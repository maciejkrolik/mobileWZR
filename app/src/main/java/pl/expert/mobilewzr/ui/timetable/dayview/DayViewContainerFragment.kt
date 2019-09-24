package pl.expert.mobilewzr.ui.timetable.dayview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_container_day_view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation
import pl.expert.mobilewzr.util.CalendarUtils

class DayViewContainerFragment : Fragment() {

    private val weekNumber = CalendarUtils.getWeekNumber()

    private lateinit var timetableViewLocation: TimetableViewLocation
    private lateinit var groupId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container_day_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromSharedPrefs()
        setupDayViewTabLayout()
        setupDayViewPagerAdapter()
        requireActivity().toolbar.toolbarTitle.text = getString(
            R.string.timetable_title_with_week, groupId, CalendarUtils.getWeekType(weekNumber)
        )
    }

    private fun getDataFromSharedPrefs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        timetableViewLocation = TimetableViewLocation.getByValue(
            arguments?.getInt("argTimetableViewLocation", TimetableViewLocation.SEARCH.value)!!
        )

        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
    }

    private fun setupDayViewTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }

    private fun setupDayViewPagerAdapter() {
        viewPager.adapter = DayViewPagerAdapter(context, timetableViewLocation, childFragmentManager)
        setDayViewViewPagerCurrentItem()
        assignOnPageChangeListener()
    }

    private fun setDayViewViewPagerCurrentItem() {
        viewPager.currentItem =
            if (weekNumber == 0) CalendarUtils.getDayOfWeek() else CalendarUtils.getDayOfWeek() + 5
    }

    private fun assignOnPageChangeListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(dayNumber: Int) {
                when (dayNumber) {
                    0, 1, 2, 3, 4 -> requireActivity().toolbar.toolbarTitle.text =
                        getString(R.string.timetable_title_week_a, groupId)
                    5, 6, 7, 8, 9 -> requireActivity().toolbar.toolbarTitle.text =
                        getString(R.string.timetable_title_week_b, groupId)
                }
            }
        })
    }

}