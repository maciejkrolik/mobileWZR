package pl.expert.mobilewzr.ui.weekview

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.databinding.FragmentWeekViewContentBinding
import javax.inject.Inject

class WeekViewContentFragment : Fragment(), WeekViewRecyclerAdapter.OnSubjectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentWeekViewContentBinding
    private lateinit var weekViewViewModel: WeekViewViewModel
    private lateinit var weekViewLocation: WeekViewLocation
    private var weekNumber: Int? = null

    private val listOfWeekViewItems: MutableList<WeekViewItem> = mutableListOf()
    private val listOfSubjects: MutableList<Subject> = mutableListOf()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewContentBinding.inflate(inflater, container, false)

        val recyclerAdapter = WeekViewRecyclerAdapter(listOfWeekViewItems, this)

        binding.weekViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        when (weekViewLocation) {
            WeekViewLocation.MY_TIMETABLE -> menu?.clear()
            WeekViewLocation.SEARCH -> inflater?.inflate(R.menu.week_view_content_menu, menu)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        weekViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(WeekViewViewModel::class.java)

        weekNumber = arguments?.getInt("argWeekNumber")!!
        weekViewLocation = WeekViewLocation.getByValue(arguments?.getInt("argWeekViewLocation")!!)

        weekViewViewModel.getWeekViewItems(weekNumber as Int).observe(viewLifecycleOwner,
            Observer<List<WeekViewItem>> { listOfWeekViewItems ->
                if (listOfWeekViewItems != null) {
                    this.listOfWeekViewItems.clear()
                    this.listOfWeekViewItems.addAll(listOfWeekViewItems)
                }

                binding.weekViewProgressBar.visibility = View.GONE
                binding.weekViewRecyclerView.visibility = View.VISIBLE
            })

        weekViewViewModel.getSubjects().observe(viewLifecycleOwner,
            Observer<List<Subject>> { listOfSubjects ->
                if (listOfSubjects != null) {
                    this.listOfSubjects.clear()
                    this.listOfSubjects.addAll(listOfSubjects)
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.set_as_my_timetable -> {
                weekViewViewModel.replaceTimetableInLocalDb()

                val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPref.edit().putString("prefSavedGroupId", weekViewViewModel.groupId).apply()

                Toast.makeText(
                    context,
                    "${getString(R.string.group)} ${weekViewViewModel.groupId} ${getString(R.string.was_saved)}",
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSubjectClick(position: Int, dayOfWeek: Int) {
        val csvIndex = listOfWeekViewItems[position].listOfSubjects[dayOfWeek].csvIndex
        if (csvIndex != -1) {
            val subject = listOfSubjects[csvIndex]
            showSubjectDetailsDialog(subject)
        }
    }

    private fun showSubjectDetailsDialog(subject: Subject) {
        val alertDialog = AlertDialog.Builder(context!!).apply {
            setTitle(subject.title)
            setMessage("${subject.location}, ${subject.description}")
        }.create()
        alertDialog.show()
    }
}
