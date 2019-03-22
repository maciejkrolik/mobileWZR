package pl.expert.mobilewzr.ui.timetableviews.dayview

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewLocation

class DayViewPagerAdapter(
    private val context: Context?,
    private val weekNumber: Int,
    private val timetableViewLocation: TimetableViewLocation,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        when (position) {
            0 -> args.putInt("argWeekDayNumber", 0)
            1 -> args.putInt("argWeekDayNumber", 1)
            2 -> args.putInt("argWeekDayNumber", 2)
            3 -> args.putInt("argWeekDayNumber", 3)
            4 -> args.putInt("argWeekDayNumber", 4)
        }
        args.putInt("argWeekNumber", weekNumber)
        args.putInt("argTimetableViewLocation", timetableViewLocation.value)

        return DayViewContentFragment().apply {
            arguments = args
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context?.getString(R.string.monday)
            1 -> context?.getString(R.string.tuesday)
            2 -> context?.getString(R.string.wednesday)
            3 -> context?.getString(R.string.thursday)
            4 -> context?.getString(R.string.friday)
            else -> null
        }
    }
}