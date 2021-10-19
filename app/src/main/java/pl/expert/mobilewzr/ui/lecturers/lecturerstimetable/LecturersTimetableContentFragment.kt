package pl.expert.mobilewzr.ui.lecturers.lecturerstimetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_week_view_content.*
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.databinding.FragmentWeekViewContentBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.ResourceState
import pl.expert.mobilewzr.util.setAttrsAndShow

class LecturersTimetableContentFragment : BaseInjectedFragment() {

    private lateinit var binding: FragmentWeekViewContentBinding
    private lateinit var viewModel: LecturersTimetableViewModel

    private val weekNumber: Int by lazy { arguments?.getInt("argWeekNumber", 0)!! }

    private val subjects: MutableList<Subject> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWeekViewContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        timetableGrid.setListener { subjectIndex ->
            showSimpleSubjectDetailsDialog(subjects[subjectIndex])
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LecturersTimetableViewModel::class.java)

        viewModel.subjectsState.observe(viewLifecycleOwner, { resourceState ->
            when (resourceState) {
                is ResourceState.Success -> {
                    val weekSpecificSubjects = viewModel.getWeekSpecificSubjects(weekNumber)
                    this.subjects.clear()
                    this.subjects.addAll(weekSpecificSubjects)
                    timetableGrid.setSubjects(weekSpecificSubjects)

                    weekViewProgressBar.visibility = View.GONE
                    weekViewGrid.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showSimpleSubjectDetailsDialog(subject: Subject) {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(subject.title)
            setMessage("${subject.startTime}-${subject.endTime}, ${subject.location}, ${subject.description}")
        }.create()
        alertDialog.setAttrsAndShow()
    }

}