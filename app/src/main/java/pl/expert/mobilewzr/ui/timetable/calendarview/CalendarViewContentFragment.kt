package pl.expert.mobilewzr.ui.timetable.calendarview

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.toolbar.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentCalendarViewContentBinding
import pl.expert.mobilewzr.domain.domainmodel.DayViewDataHolder
import pl.expert.mobilewzr.domain.domainmodel.SubjectItem
import pl.expert.mobilewzr.ui.timetable.TimetableContentBaseFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewRecyclerAdapter
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewViewModel
import pl.expert.mobilewzr.util.CalendarUtils
import java.util.*

class CalendarViewContentFragment : TimetableContentBaseFragment(), DayViewRecyclerAdapter.OnSubjectListener {

    private lateinit var binding: FragmentCalendarViewContentBinding
    private lateinit var dayViewViewModel: DayViewViewModel
    private lateinit var dayViewDataHolder: DayViewDataHolder
    private lateinit var groupId: String
    private lateinit var recyclerAdapter: DayViewRecyclerAdapter

    private val subjects: MutableList<SubjectItem> = mutableListOf()

    private val calendar: Calendar = CalendarUtils.getCalendar()
    private var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    private var month: Int = calendar.get(Calendar.MONTH)
    private var year: Int = calendar.get(Calendar.YEAR)
    private var weekNumber: Int = CalendarUtils.getWeekNumber()
    private var weekDayNumber: Int = CalendarUtils.getDayOfWeek()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalendarViewContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getDataFromSharedPrefs()
        setupViewModel()
        setTitle(day, month, weekDayNumber)
    }

    private fun setupRecyclerView() {
        recyclerAdapter = DayViewRecyclerAdapter(subjects, this)
        binding.dayViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    private fun getDataFromSharedPrefs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
    }

    private fun setupViewModel() {
        dayViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(DayViewViewModel::class.java)

        dayViewViewModel.daySubjects.observe(viewLifecycleOwner, Observer { subjects ->
            if (subjects.isNotEmpty()) {
                this.subjects.clear()
                this.subjects.addAll(subjects)

                recyclerAdapter.notifyDataSetChanged()

                binding.dayViewProgressBar.visibility = View.GONE
                binding.dayViewInfoText.visibility = View.GONE
                binding.dayViewRecyclerView.visibility = View.VISIBLE
            } else {
                binding.dayViewProgressBar.visibility = View.GONE
                binding.dayViewRecyclerView.visibility = View.GONE
                binding.dayViewInfoText.visibility = View.VISIBLE
            }
        })

        dayViewViewModel.getDayViewDataHolder().observe(viewLifecycleOwner,
            Observer { dayViewDataHolder ->
                if (dayViewDataHolder != null) {
                    this.dayViewDataHolder = dayViewDataHolder
                    dayViewViewModel.getCalendarDayViewItems(weekNumber, weekDayNumber)
                }
            })
    }

    override fun onSubjectClick(position: Int) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.chooseDay -> {
                showDatePickerDialog()
                return true
            }
            R.id.choose_view -> {
                chooseView(dayViewViewModel.groupId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.AlertDialogTheme,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val dayOfWeek = CalendarUtils.getDayOfWeek(day, month, year)
                setTitle(day, month, dayOfWeek)

                this.year = year
                this.month = month
                this.day = day
                this.weekDayNumber = CalendarUtils.getDayOfWeek(day, month, year)

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, day)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                this.weekNumber = CalendarUtils.getWeekNumber(calendar.time)

                dayViewViewModel.getCalendarDayViewItems(weekNumber, weekDayNumber)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun setTitle(day: Int, month: Int, weekDayNumber: Int) {
        val weekDay = when (weekDayNumber) {
            0 -> context?.getString(R.string.monday_short)
            1 -> context?.getString(R.string.tuesday_short)
            2 -> context?.getString(R.string.wednesday_short)
            3 -> context?.getString(R.string.thursday_short)
            4 -> context?.getString(R.string.friday_short)
            5 -> context?.getString(R.string.saturday_short)
            6 -> context?.getString(R.string.sunday_short)
            else -> ""
        }

        val dayString = day.toString().padStart(2, '0')
        val monthString = (month + 1).toString().padStart(2, '0')

        val mergedDate = "$dayString.$monthString"

        requireActivity().toolbarTitle.text =
            requireContext().getString(R.string.timetable_title_calendar, groupId, weekDay, mergedDate)
    }

}