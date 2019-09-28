package pl.expert.mobilewzr.ui.timetable.editview

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
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
import pl.expert.mobilewzr.ui.timetable.TimetableViewModel
import pl.expert.mobilewzr.ui.timetable.TimetableViewType
import pl.expert.mobilewzr.util.CalendarUtils
import java.util.*

class EditViewFragment : BaseInjectedFragment() {

    private val editViewViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(EditViewViewModel::class.java)
    }
    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(TimetableViewModel::class.java)
    }
    private val subjectIndex by lazy { arguments?.getInt("argSubjectIndex") ?: -1 }
    private val weekNumber by lazy { arguments?.getInt("argWeekNumber") ?: 0 }
    private val weekDayNumber by lazy { arguments?.getInt("argWeekDayNumber") ?: 0 }

    private lateinit var binding: FragmentEditViewBinding
    private lateinit var firstWeekMondayDate: Date
    private lateinit var timetableViewType: TimetableViewType
    private lateinit var subject: Subject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.edit_view_menu, menu)
        if (subjectIndex == -1) {
            val deleteItem = menu.findItem(R.id.delete)
            deleteItem?.isVisible = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromSharedPrefs()
        setTitle()
        getSubjectsAndObserveThem()
        getStateAndObserveIt()
        setOnMultipleUpdateSwitchListener()
        setOnClickListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                editViewViewModel.deleteSubject(subjectIndex)
                return true
            }
            R.id.save -> {
                val fieldsAreNotBlank = checkIfFieldsAreNotBlank()
                val timeIsValid = checkIfTimeIsValid()
                if (fieldsAreNotBlank && timeIsValid) {
                    val newSubject = prepareSubject()
                    if (subjectIndex != -1 && !copyModeSwitch.isChecked)
                        updateSubjectsWith(newSubject)
                    else
                        editViewViewModel.addSubject(newSubject)
                } else {
                    if (!fieldsAreNotBlank) {
                        Toast.makeText(context, getString(R.string.fields_cannot_be_empty), Toast.LENGTH_SHORT).show()
                        return true
                    }
                    if (!timeIsValid) {
                        Toast.makeText(context, getString(R.string.time_is_incorect), Toast.LENGTH_SHORT).show()
                        return true
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDataFromSharedPrefs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        timetableViewType =
            TimetableViewType.getByValue(sharedPref.getInt("prefTimetableViewType", TimetableViewType.DAY_VIEW.value))
    }

    private fun setTitle() {
        if (subjectIndex != -1)
            toolbar.toolbarTitle.text = getString(R.string.edit)
        else
            toolbar.toolbarTitle.text = getString(R.string.add)
    }

    private fun setOnClickListeners() {
        editViewStartTime.setOnClickListener {
            showTimePicker(editViewStartTime, editViewStartTime.text.toString())
        }

        editViewEndTime.setOnClickListener {
            showTimePicker(editViewEndTime, editViewEndTime.text.toString())
        }
    }

    private fun showTimePicker(editView: TextView, subjectTime: String) {
        val hour = subjectTime.substringBefore(".").toInt()
        val minute = subjectTime.substringAfter(".").toInt()

        val timePicker = TimePickerDialog(context, R.style.AlertDialogTheme, { _, selectedHour, selectedMinute ->

            val hourString = selectedHour.toString().padStart(2, '0')
            val minuteString = selectedMinute.toString().padStart(2, '0')

            editView.text = requireContext().getString(R.string.time_string, hourString, minuteString)

        }, hour, minute, true)

        timePicker.show()
    }

    private fun checkIfFieldsAreNotBlank(): Boolean {
        return (titleEditText.text.isNotBlank()
                && descriptionEditText.text.isNotBlank()
                && locationEditText.text.isNotBlank())
    }

    private fun checkIfTimeIsValid(): Boolean {
        val startTimeMinutes = CalendarUtils.getMinutesFromTimeString(editViewStartTime.text.toString())
        val endTimeMinutes = CalendarUtils.getMinutesFromTimeString(editViewEndTime.text.toString())

        if (startTimeMinutes >= endTimeMinutes)
            return false
        if (startTimeMinutes < 480)
            return false
        if (endTimeMinutes > 1260)
            return false

        return true
    }

    private fun prepareSubject(): Subject {
        val newSubject = Subject()
        newSubject.title = titleEditText.text.toString()
        newSubject.description = descriptionEditText.text.toString()
        newSubject.location = locationEditText.text.toString()
        newSubject.startTime = editViewStartTime.text.toString()
        newSubject.endTime = editViewEndTime.text.toString()
        newSubject.startDate = getStartDate(dayWeekSegmentedGroup.position, weekSegmentedGroup.position)
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
            editViewViewModel.updateMultipleSubjects(subjectIndex, newSubject)
        else
            editViewViewModel.updateSubject(subjectIndex, newSubject)
    }

    @SuppressLint("SetTextI18n")
    private fun getSubjectsAndObserveThem() {
        val groupId =
            if (timetableViewType == TimetableViewType.DAY_VIEW || timetableViewType == TimetableViewType.WEEK_VIEW) {
                viewModel.groupId
            } else {
                ""
            }
        editViewViewModel.getSubjects(groupId).observe(viewLifecycleOwner,
            Observer
            { subjects ->
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
                    dayWeekSegmentedGroup.setPosition(weekDayNumber, false)
                    weekSegmentedGroup.setPosition(weekNumber, false)
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
                    viewModel.reloadSubjects()
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
