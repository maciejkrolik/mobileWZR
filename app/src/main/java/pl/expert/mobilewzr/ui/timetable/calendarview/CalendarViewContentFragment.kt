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
import pl.expert.mobilewzr.domain.domainmodel.SubjectItem
import pl.expert.mobilewzr.domain.domainmodel.TimetableDataHolder
import pl.expert.mobilewzr.ui.timetable.TimetableContentBaseFragment
import pl.expert.mobilewzr.ui.timetable.TimetableViewModel
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewRecyclerAdapter
import pl.expert.mobilewzr.util.CalendarUtils
import pl.expert.mobilewzr.util.ResourceState
import java.util.*

class CalendarViewContentFragment : TimetableContentBaseFragment(), DayViewRecyclerAdapter.OnSubjectListener {

    private lateinit var binding: FragmentCalendarViewContentBinding
    private lateinit var viewModel: TimetableViewModel
    private lateinit var recyclerAdapter: DayViewRecyclerAdapter
    private lateinit var timetableDataHolder: TimetableDataHolder
    private lateinit var groupId: String

    private val subjectItems: MutableList<SubjectItem> = mutableListOf()

    private val calendar: Calendar = CalendarUtils.getCalendar()
    private var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    private var month: Int = calendar.get(Calendar.MONTH)
    private var year: Int = calendar.get(Calendar.YEAR)
    private var weekNumber: Int = CalendarUtils.getWeekNumber()
    private var weekDayNumber: Int = CalendarUtils.getDayOfWeek()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalendarViewContentBinding.inflate(inflater, container, false)

        if (savedInstanceState != null) {
            day = savedInstanceState.getInt("day")
            month = savedInstanceState.getInt("month")
            year = savedInstanceState.getInt("year")
            weekNumber = savedInstanceState.getInt("weekNumber")
            weekDayNumber = savedInstanceState.getInt("weekDayNumber")
        }

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
        recyclerAdapter = DayViewRecyclerAdapter(subjectItems, this)
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
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(TimetableViewModel::class.java)

        viewModel.timetableDataHolder.observe(viewLifecycleOwner, Observer { resourceState ->
            when (resourceState) {
                is ResourceState.Success -> {
                    this.timetableDataHolder = resourceState.data!!
                    refreshSubjectItems()
                }
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
                chooseView(viewModel.groupId)
                return true
            }
            R.id.set_as_my_timetable -> {
                viewModel.replaceSubjectsInDb()
                putIdOfAGroupSavedInDbIntoSharedPref(viewModel.groupId)
                showToast(viewModel.groupId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerTheme,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val dayOfWeek = CalendarUtils.getDayOfWeek(day, month, year)
                setTitle(day, month, dayOfWeek)

                this.year = year
                this.month = month
                this.day = day
                this.weekDayNumber = CalendarUtils.getDayOfWeek(day, month, year)

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.MONTH, month)
                    set(Calendar.YEAR, year)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                this.weekNumber = CalendarUtils.getWeekNumber(calendar.time)

                refreshSubjectItems(calendar.time)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun refreshSubjectItems(date: Date = Date()) {
        val dayViewItems = viewModel.getCalendarDayViewItems(weekNumber, weekDayNumber, date)
        if (dayViewItems.isNotEmpty()) {
            this.subjectItems.clear()
            this.subjectItems.addAll(dayViewItems)

            recyclerAdapter.notifyDataSetChanged()

            binding.dayViewProgressBar.visibility = View.GONE
            binding.dayViewInfoText.visibility = View.GONE
            binding.dayViewRecyclerView.visibility = View.VISIBLE
        } else {
            binding.dayViewProgressBar.visibility = View.GONE
            binding.dayViewRecyclerView.visibility = View.GONE
            binding.dayViewInfoText.visibility = View.VISIBLE
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("day", day)
        outState.putInt("month", month)
        outState.putInt("year", year)
        outState.putInt("weekNumber", weekNumber)
        outState.putInt("weekDayNumber", weekDayNumber)
        super.onSaveInstanceState(outState)
    }

}