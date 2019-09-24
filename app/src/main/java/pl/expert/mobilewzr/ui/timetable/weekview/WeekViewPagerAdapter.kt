package pl.expert.mobilewzr.ui.timetable.weekview

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation

class WeekViewPagerAdapter(
    private val context: Context?,
    private val timetableViewLocation: TimetableViewLocation,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        when (position) {
            0 -> args.putInt("argWeekNumber", 0)
            1 -> args.putInt("argWeekNumber", 1)
        }
        args.putInt("argTimetableViewLocation", timetableViewLocation.value)

        return WeekViewContentFragment().apply {
            arguments = args
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context?.getString(R.string.weekA)
            1 -> context?.getString(R.string.weekB)
            else -> null
        }
    }

}