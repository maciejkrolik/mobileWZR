package pl.expert.mobilewzr.ui.timetableviews.weekview

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.databinding.FragmentWeekViewContentBinding
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewLocation
import javax.inject.Inject

class WeekViewContentFragment : Fragment(), WeekViewRecyclerAdapter.OnSubjectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentWeekViewContentBinding
    private lateinit var weekViewViewModel: WeekViewViewModel
    private lateinit var timetableViewLocation: TimetableViewLocation
    private var weekNumber: Int? = null

    private val weekViewItems: MutableList<WeekViewItem> = mutableListOf()
    private val subjects: MutableList<Subject> = mutableListOf()

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

        weekNumber = arguments?.getInt("argWeekNumber")!!
        timetableViewLocation = TimetableViewLocation.getByValue(arguments?.getInt("argTimetableViewLocation")!!)

        val recyclerAdapter = WeekViewRecyclerAdapter(weekViewItems, this)
        binding.weekViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        weekViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(WeekViewViewModel::class.java)

        weekViewViewModel.getWeekViewItems(weekNumber as Int).observe(viewLifecycleOwner,
            Observer<List<WeekViewItem>> { weekViewItems ->
                if (weekViewItems != null) {
                    this.weekViewItems.clear()
                    this.weekViewItems.addAll(weekViewItems)
                }

                binding.weekViewProgressBar.visibility = View.GONE
                binding.weekViewRecyclerView.visibility = View.VISIBLE
            })

        weekViewViewModel.getSubjects().observe(viewLifecycleOwner,
            Observer<List<Subject>> { subjects ->
                if (subjects != null) {
                    this.subjects.clear()
                    this.subjects.addAll(subjects)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        when (timetableViewLocation) {
            TimetableViewLocation.MY_TIMETABLE -> menu?.clear()
            TimetableViewLocation.SEARCH -> inflater?.inflate(R.menu.week_view_content_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.set_as_my_timetable -> {
                weekViewViewModel.replaceSubjectsInDb()
                putIdOfAGroupSavedInDbIntoSharedPref()
                showToast()
                return true
            }
            R.id.settings -> {
                Navigation.findNavController(view!!)
                    .navigate(R.id.action_search_timetable_view_fragment_to_settings_view_fragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun putIdOfAGroupSavedInDbIntoSharedPref() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPref.edit().putString("prefIdOfAGroupSavedInDb", weekViewViewModel.groupId).apply()
    }

    private fun showToast() {
        Toast.makeText(
            context,
            "${getString(R.string.group)} ${weekViewViewModel.groupId} ${getString(R.string.was_saved)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSubjectClick(position: Int, dayOfWeek: Int) {
        val subjectIndex = weekViewItems[position].weekViewSubjectItems[dayOfWeek].index
        if (subjectIndex != -1) {
            val subject = subjects[subjectIndex]
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
