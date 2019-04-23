package pl.expert.mobilewzr.ui.timetableviews.editview

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.databinding.FragmentEditViewBinding
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewType
import pl.expert.mobilewzr.ui.timetableviews.dayview.DayViewViewModel
import pl.expert.mobilewzr.ui.timetableviews.weekview.WeekViewViewModel
import pl.expert.mobilewzr.util.CalendarUtils
import java.util.*
import javax.inject.Inject

class EditViewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val editViewViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(EditViewViewModel::class.java)
    }
    private val dayViewViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(DayViewViewModel::class.java)
    }
    private val weekViewViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(WeekViewViewModel::class.java)
    }

    private lateinit var binding: FragmentEditViewBinding
    private lateinit var firstSubjectStartDate: Date
    private lateinit var timetableViewType: TimetableViewType
    private var subjectIndex: Int? = null
    private var weekNumber: Int? = null
    private var weekDayNumber: Int? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditViewBinding.inflate(inflater, container, false)

        activity?.title = ""

        subjectIndex = arguments?.getInt("argSubjectIndex") ?: -1
        weekNumber = arguments?.getInt("argWeekNumber") ?: 0
        weekDayNumber = arguments?.getInt("argWeekDayNumber") ?: 0
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        timetableViewType =
            TimetableViewType.getByValue(
                sharedPref.getString(
                    "prefTimetableViewType",
                    TimetableViewType.DAY_VIEW.value.toString()
                ).toInt()
            )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getSubjectsAndObserveThem()
        getStateAndObserveIt()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_view_menu, menu)
        if (subjectIndex == -1) {
            val deleteItem = menu?.findItem(R.id.delete)
            deleteItem?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> {
                editViewViewModel.deleteSubject(subjectIndex!!)
                return true
            }
            R.id.save -> {
                val newSubject = prepareSubject()
                if (subjectIndex != -1)
                    editViewViewModel.updateSubject(subjectIndex!!, newSubject)
                else
                    editViewViewModel.addSubject(newSubject)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun prepareSubject(): Subject {
        val newSubject = Subject()
        newSubject.title = binding.titleEditText.text.toString()
        newSubject.description = binding.descriptionEditText.text.toString()
        newSubject.startTime =
            CalendarUtils.getSubjectTimeStringFrom(binding.chooseTimeSpinner.selectedItemPosition)
        newSubject.startDate = getStartDate(
            binding.chooseDaySpinner.selectedItemPosition,
            binding.chooseWeekSpinner.selectedItemPosition
        )
        return newSubject
    }

    private fun getStartDate(day: Int, week: Int): Date {
        val shiftStartDateBy = if (week == 0) day else day + 7
        val calendar = Calendar.getInstance(Locale.UK)
        calendar.time = firstSubjectStartDate
        calendar.add(Calendar.DATE, shiftStartDateBy)
        return calendar.time
    }

    private fun getSubjectsAndObserveThem() {
        val groupId = when (timetableViewType) {
            TimetableViewType.DAY_VIEW -> dayViewViewModel.groupId
            TimetableViewType.WEEK_VIEW -> weekViewViewModel.groupId
        }
        editViewViewModel.getSubjects(groupId).observe(viewLifecycleOwner,
            Observer { subjects ->
                if (subjects != null && subjectIndex != -1) {
                    val subject = subjects.single { subject -> subject.index == subjectIndex }
                    binding.titleEditText.setText(subject.title)
                    binding.descriptionEditText.setText(subject.description)
                    binding.chooseTimeSpinner.setSelection(CalendarUtils.getSubjectTimeIndexFrom(subject.startTime))
                    binding.chooseDaySpinner.setSelection(CalendarUtils.getDayOfWeek(subject.startDate))
                    binding.chooseWeekSpinner.setSelection(CalendarUtils.getWeekNumber(subject.startDate))
                } else {
                    binding.chooseDaySpinner.setSelection(weekDayNumber!!)
                    binding.chooseWeekSpinner.setSelection(weekNumber!!)
                }
                // TODO Fix first() returns always first Monday
                firstSubjectStartDate = subjects.first().startDate
                hideProgressBar()
            })
    }

    private fun getStateAndObserveIt() {
        var wasUpdatedBefore = false
        editViewViewModel.isUpdatingDb.observe(viewLifecycleOwner,
            Observer { updatingDb ->
                if (!updatingDb && wasUpdatedBefore) {
                    when (timetableViewType) {
                        TimetableViewType.DAY_VIEW -> dayViewViewModel.reloadSubjects()
                        TimetableViewType.WEEK_VIEW -> weekViewViewModel.reloadSubjects()
                    }
                    Navigation.findNavController(view!!).popBackStack()
                } else if (updatingDb) {
                    wasUpdatedBefore = true
                }
            })
    }

    private fun hideProgressBar() {
        binding.editViewProgressBar.visibility = View.GONE
        binding.editTitleLabel.visibility = View.VISIBLE
        binding.titleEditText.visibility = View.VISIBLE
        binding.editDescriptionLabel.visibility = View.VISIBLE
        binding.descriptionEditText.visibility = View.VISIBLE
        binding.chooseTimeLabel.visibility = View.VISIBLE
        binding.chooseTimeSpinner.visibility = View.VISIBLE
        binding.chooseDayLabel.visibility = View.VISIBLE
        binding.chooseDaySpinner.visibility = View.VISIBLE
        binding.chooseWeekLabel.visibility = View.VISIBLE
        binding.chooseWeekSpinner.visibility = View.VISIBLE
    }
}
