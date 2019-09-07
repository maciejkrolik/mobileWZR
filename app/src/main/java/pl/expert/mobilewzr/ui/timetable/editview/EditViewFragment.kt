package pl.expert.mobilewzr.ui.timetable.editview

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_edit_view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.databinding.FragmentEditViewBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.ui.timetable.TimetableViewType
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewViewModel
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewViewModel
import pl.expert.mobilewzr.util.CalendarUtils
import java.util.*

class EditViewFragment : BaseInjectedFragment() {

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
    private lateinit var firstWeekMondayDate: Date
    private lateinit var timetableViewType: TimetableViewType
    private lateinit var subject: Subject
    private var subjectIndex: Int? = null
    private var weekNumber: Int? = null
    private var weekDayNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditViewBinding.inflate(inflater, container, false)

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
                )!!.toInt()
            )

        if (subjectIndex != -1)
            toolbar.toolbarTitle.text = getString(R.string.edit)
        else
            toolbar.toolbarTitle.text = getString(R.string.add)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getSubjectsAndObserveThem()
        getStateAndObserveIt()
        setOnMultipleUpdateSwitchListener()
        setOnClickListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_view_menu, menu)
        if (subjectIndex == -1) {
            val deleteItem = menu.findItem(R.id.delete)
            deleteItem?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                editViewViewModel.deleteSubject(subjectIndex!!)
                return true
            }
            R.id.save -> {
                val fieldsAreBlank = checkIfFieldsAreBlank()
                if (!fieldsAreBlank) {
                    val newSubject = prepareSubject()
                    if (subjectIndex != -1 && !copyModeSwitch.isChecked)
                        updateSubjectsWith(newSubject)
                    else
                        editViewViewModel.addSubject(newSubject)
                } else {
                    Toast.makeText(context, getString(R.string.fields_cannot_be_empty), Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setOnClickListeners() {
        editViewStartTime.setOnClickListener {
            val hour = subject.startTime.substringBefore(".").toInt()
            val minute = subject.startTime.substringAfter(".").toInt()

            val timePicker = TimePickerDialog(context, R.style.AlertDialogTheme, { _, selectedHour, selectedMinute ->

                editViewStartTime.text =
                    "${selectedHour.toString().padStart(2, '0')}.${selectedMinute.toString().padStart(2, '0')}"

            }, hour, minute, true)

            timePicker.show()
        }

        editViewEndTime.setOnClickListener {
            val hour = subject.endTime.substringBefore(".").toInt()
            val minute = subject.endTime.substringAfter(".").toInt()

            val timePicker = TimePickerDialog(context, R.style.AlertDialogTheme, { _, selectedHour, selectedMinute ->

                editViewEndTime.text =
                    "${selectedHour.toString().padStart(2, '0')}.${selectedMinute.toString().padStart(2, '0')}"

            }, hour, minute, true)

            timePicker.show()
        }
    }

    private fun checkIfFieldsAreBlank(): Boolean {
        return (titleEditText.text.isBlank()
                || descriptionEditText.text.isBlank()
                || locationEditText.text.isBlank())
    }

    private fun prepareSubject(): Subject {
        val newSubject = Subject()
        newSubject.title = titleEditText.text.toString()
        newSubject.description = descriptionEditText.text.toString()
        newSubject.location = locationEditText.text.toString()
        newSubject.startTime = editViewStartTime.text.toString()
        newSubject.endTime = editViewEndTime.text.toString()
        newSubject.startDate = getStartDate(
            dayWeekSegmentedGroup.position,
            weekSegmentedGroup.position
        )
        return newSubject
    }

    private fun getStartDate(day: Int, week: Int): Date {
        val shiftStartDateBy = if (week == 0) day else day + 7
        val calendar = Calendar.getInstance(Locale.UK)
        calendar.time = firstWeekMondayDate
        calendar.add(Calendar.DATE, shiftStartDateBy)
        return calendar.time
    }

    private fun updateSubjectsWith(newSubject: Subject) {
        if (updateMultipleSubjectsSwitch.isChecked)
            editViewViewModel.updateMultipleSubjects(subjectIndex!!, newSubject)
        else
            editViewViewModel.updateSubject(subjectIndex!!, newSubject)
    }

    @SuppressLint("SetTextI18n")
    private fun getSubjectsAndObserveThem() {
        val groupId = when (timetableViewType) {
            TimetableViewType.DAY_VIEW -> dayViewViewModel.groupId
            TimetableViewType.WEEK_VIEW -> weekViewViewModel.groupId
        }
        editViewViewModel.getSubjects(groupId).observe(viewLifecycleOwner,
            Observer { subjects ->
                if (subjects != null && subjectIndex != -1) {
                    subject = subjects.single { subject -> subject.index == subjectIndex }
                    titleEditText.setText(subject.title)
                    descriptionEditText.setText(subject.description)
                    locationEditText.setText(subject.location)
                    dayWeekSegmentedGroup.setPosition(CalendarUtils.getDayOfWeek(subject.startDate), false)
                    weekSegmentedGroup.setPosition(CalendarUtils.getWeekNumber(subject.startDate), false)
                    editViewStartTime.text = subject.startTime
                    editViewEndTime.text = subject.endTime
                } else {
                    editViewStartTime.text = "08.00"
                    editViewEndTime.text = "09.30"
                    dayWeekSegmentedGroup.setPosition(weekDayNumber!!, false)
                    weekSegmentedGroup.setPosition(weekNumber!!, false)
                }
                firstWeekMondayDate = getFirstWeekMondayDate(subjects.first())
            })
    }

    private fun getFirstWeekMondayDate(firstSubject: Subject): Date {
        val calendar = Calendar.getInstance()
        calendar.time = firstSubject.startDate
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return calendar.time
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
                    if (copyModeSwitch.isChecked) {
                        Toast.makeText(context, getString(R.string.classes_has_been_copied), Toast.LENGTH_SHORT).show()
                    } else {
                        findNavController().popBackStack()
                    }
                } else if (updatingDb) {
                    wasUpdatedBefore = true
                }
            })
    }

    private fun setOnMultipleUpdateSwitchListener() {
        updateMultipleSubjectsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                timeChooser.visibility = View.GONE
                dayWeekSegmentedGroup.visibility = View.GONE
                weekSegmentedGroup.visibility = View.GONE
                copyModeSwitch.visibility = View.GONE
                copyModeSwitch.isChecked = false
            } else {
                timeChooser.visibility = View.VISIBLE
                dayWeekSegmentedGroup.visibility = View.VISIBLE
                weekSegmentedGroup.visibility = View.VISIBLE
                copyModeSwitch.visibility = View.VISIBLE
            }
        }
    }

}
