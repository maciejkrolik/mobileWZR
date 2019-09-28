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
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation
import pl.expert.mobilewzr.ui.timetable.TimetableViewModel
import pl.expert.mobilewzr.util.ResourceState
import pl.expert.mobilewzr.util.setAttrsAndShow

class WeekViewContentFragment : TimetableContentBaseFragment() {

    private lateinit var binding: FragmentWeekViewContentBinding
    private lateinit var viewModel: TimetableViewModel
    private val weekNumber: Int by lazy { arguments?.getInt("argWeekNumber", 0)!! }

    private val subjects: MutableList<Subject> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()

        timetableGrid.setListener { subjectIndex ->
            if (timetableViewLocation == TimetableViewLocation.SEARCH)
                showSimpleSubjectDetailsDialog(subjects[subjectIndex])
            else
                showSubjectDetailsDialog(subjects[subjectIndex])
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(TimetableViewModel::class.java)

        viewModel.timetableDataHolder.observe(viewLifecycleOwner, Observer { resourceState ->
            when (resourceState) {
                is ResourceState.Success -> {
                    val weekSubjects = viewModel.getWeekSpecificSubjects(weekNumber)
                    this.subjects.clear()
                    this.subjects.addAll(weekSubjects)
                    timetableGrid.setSubjects(weekSubjects)

                    weekViewProgressBar.visibility = View.GONE
                    weekViewGrid.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_as_my_timetable -> {
                viewModel.replaceSubjectsInDb()
                putIdOfAGroupSavedInDbIntoSharedPref(viewModel.groupId)
                showToast(viewModel.groupId)
                return true
            }
            R.id.add_new_subject -> {
                val args = Bundle()
                args.putInt("argSubjectIndex", -1)
                args.putInt("argWeekNumber", weekNumber)
                findNavController().navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
                return true
            }
            R.id.choose_view -> {
                chooseView(viewModel.groupId)
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
            setPositiveButton(R.string.edit) { _, _ ->
                navigateToEditFragment(subject.index)
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
        }.create()
        alertDialog.setAttrsAndShow()
    }

    private fun showSimpleSubjectDetailsDialog(subject: Subject) {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(subject.title)
            setMessage("${subject.location}, ${subject.description}")
        }.create()
        alertDialog.setAttrsAndShow()
    }

}
