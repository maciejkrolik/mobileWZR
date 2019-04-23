package pl.expert.mobilewzr.ui.timetableviews.weekview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.databinding.FragmentWeekViewContentBinding
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewContentBaseFragment
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewLocation
import javax.inject.Inject

class WeekViewContentFragment : TimetableViewContentBaseFragment(), WeekViewRecyclerAdapter.OnSubjectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentWeekViewContentBinding
    private lateinit var weekViewViewModel: WeekViewViewModel
    private var weekNumber: Int? = null

    private val weekViewItems: MutableList<WeekViewItem> = mutableListOf()
    private val subjects: MutableList<Subject> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewContentBinding.inflate(inflater, container, false)

        weekNumber = arguments?.getInt("argWeekNumber")!!

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.set_as_my_timetable -> {
                weekViewViewModel.replaceSubjectsInDb()
                putIdOfAGroupSavedInDbIntoSharedPref(weekViewViewModel.groupId)
                showToast(weekViewViewModel.groupId)
                return true
            }
            R.id.add_new_subject -> {
                val args = Bundle()
                args.putInt("argSubjectIndex", -1)
                args.putInt("argWeekNumber", weekNumber ?: 0)
                Navigation.findNavController(view!!)
                    .navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSubjectClick(position: Int, dayOfWeek: Int) {
        val subjectIndex = weekViewItems[position].weekViewSubjectItems[dayOfWeek].index
        if (subjectIndex != -1) {
            val subject = subjects.single { subject -> subject.index == subjectIndex }
            showSubjectDetailsDialog(subject)
        }
    }

    override fun onSubjectLongClick(position: Int, dayOfWeek: Int) {
        val subjectIndex = weekViewItems[position].weekViewSubjectItems[dayOfWeek].index
        if (subjectIndex != -1 && timetableViewLocation == TimetableViewLocation.MY_TIMETABLE) {
            navigateToEditFragment(subjectIndex)
        }
    }

    private fun navigateToEditFragment(subjectIndex: Int) {
        val args = Bundle()
        args.putInt("argSubjectIndex", subjectIndex)
        Navigation.findNavController(view!!).navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
    }

    private fun showSubjectDetailsDialog(subject: Subject) {
        val alertDialog = AlertDialog.Builder(context!!).apply {
            setTitle(subject.title)
            setMessage("${subject.location}, ${subject.description}")
        }.create()
        alertDialog.show()
    }
}
