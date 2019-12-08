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
import pl.expert.mobilewzr.util.addCurrentDayOrWeekIndicator

class DayViewContainerFragment : Fragment() {

    private val weekNumber = CalendarUtils.getWeekNumber()
    private val title by lazy { requireActivity().toolbar.toolbarTitle }

    private lateinit var timetableViewLocation: TimetableViewLocation
    private lateinit var groupId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container_day_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromSharedPrefs()
        title.text = getString(R.string.timetable_title_with_week, groupId, CalendarUtils.getWeekType(weekNumber))
        setupDayViewTabLayout()
        setupDayViewPagerAdapter()
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
        val dayOfWeek = if (weekNumber == 0) CalendarUtils.getDayOfWeek() else CalendarUtils.getDayOfWeek() + 5
        if (CalendarUtils.getDayOfWeek() in listOf(5, 6)) {
            if (weekNumber == 0) {
                viewPager.currentItem = 5
                title.text = getString(R.string.timetable_title_with_week, groupId, CalendarUtils.getWeekType(1))
            } else {
                viewPager.currentItem = 0
                title.text = getString(R.string.timetable_title_with_week, groupId, CalendarUtils.getWeekType(0))
            }
        } else {
            viewPager.currentItem = dayOfWeek
            tabLayout.addCurrentDayOrWeekIndicator(requireContext(), dayOfWeek)
        }
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
                    0, 1, 2, 3, 4 -> title.text = getString(R.string.timetable_title_week_a, groupId)
                    5, 6, 7, 8, 9 -> title.text = getString(R.string.timetable_title_week_b, groupId)
                }
            }
        })
    }

}