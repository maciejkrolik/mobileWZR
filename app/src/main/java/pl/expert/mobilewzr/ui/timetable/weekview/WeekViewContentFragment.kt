package pl.expert.mobilewzr.ui.timetable.weekview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_week_view_content.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.databinding.FragmentWeekViewContentBinding
import pl.expert.mobilewzr.ui.timetable.TimetableContentBaseFragment

class WeekViewContentFragment : TimetableContentBaseFragment() {

    private lateinit var binding: FragmentWeekViewContentBinding
    private lateinit var weekViewViewModel: WeekViewViewModel
    private var weekNumber: Int? = null

    private val subjects: MutableList<Subject> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewContentBinding.inflate(inflater, container, false)

        weekNumber = arguments?.getInt("argWeekNumber")!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(WeekViewViewModel::class.java)

        weekViewViewModel.getSpecificWeekSubjects(weekNumber!!).observe(viewLifecycleOwner,
            Observer<List<Subject>> { subjects ->
                if (subjects != null) {
                    this.subjects.clear()
                    this.subjects.addAll(subjects)

                    timetableGrid.setSubjects(subjects)
                }
            })

        timetableGrid.setListener { subjectIndex ->
            showSubjectDetailsDialog(subjects[subjectIndex])
        }
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
                findNavController().navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToEditFragment(subjectIndex: Int) {
        val args = Bundle()
        args.putInt("argSubjectIndex", subjectIndex)
        findNavController().navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
    }

    private fun showSubjectDetailsDialog(subject: Subject) {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(subject.title)
            setMessage("${subject.location}, ${subject.description}")
        }.create()
        alertDialog.show()
    }

}
