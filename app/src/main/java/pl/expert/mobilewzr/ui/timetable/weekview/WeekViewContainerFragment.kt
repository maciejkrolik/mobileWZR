package pl.expert.mobilewzr.ui.timetable.weekview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_container_day_view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation
import pl.expert.mobilewzr.util.CalendarUtils

class WeekViewContainerFragment : Fragment() {

    private val weekNumber = CalendarUtils.getWeekNumber()

    private lateinit var timetableViewLocation: TimetableViewLocation
    private lateinit var groupId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container_week_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromSharedPrefs()
        setupDayViewTabLayout()
        setupWeekViewPagerAdapter()
        requireActivity().toolbar.toolbarTitle.text = getString(R.string.timetable_title_base, groupId)
    }

    private fun getDataFromSharedPrefs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        timetableViewLocation = TimetableViewLocation.getByValue(
            arguments?.getInt(
                "argTimetableViewLocation", TimetableViewLocation.SEARCH.value
            )!!
        )

        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
    }

    private fun setupDayViewTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupWeekViewPagerAdapter() {
        viewPager.adapter = WeekViewPagerAdapter(context, timetableViewLocation, childFragmentManager)
        viewPager.currentItem = weekNumber
    }

}