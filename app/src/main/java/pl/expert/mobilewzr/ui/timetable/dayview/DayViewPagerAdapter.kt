package pl.expert.mobilewzr.ui.timetable.dayview

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation

class DayViewPagerAdapter(
    private val context: Context?,
    private val timetableViewLocation: TimetableViewLocation,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        when (position) {
            0 -> addDaySpecificBundleArgs(args, 0, 0)
            1 -> addDaySpecificBundleArgs(args, 1, 0)
            2 -> addDaySpecificBundleArgs(args, 2, 0)
            3 -> addDaySpecificBundleArgs(args, 3, 0)
            4 -> addDaySpecificBundleArgs(args, 4, 0)
            5 -> addDaySpecificBundleArgs(args, 0, 1)
            6 -> addDaySpecificBundleArgs(args, 1, 1)
            7 -> addDaySpecificBundleArgs(args, 2, 1)
            8 -> addDaySpecificBundleArgs(args, 3, 1)
            9 -> addDaySpecificBundleArgs(args, 4, 1)
        }
        args.putInt("argTimetableViewLocation", timetableViewLocation.value)

        return DayViewContentFragment().apply {
            arguments = args
        }
    }

    private fun addDaySpecificBundleArgs(args: Bundle, dayNumber: Int, weekNumber: Int) {
        args.putInt("argWeekDayNumber", dayNumber)
        args.putInt("argWeekNumber", weekNumber)
    }

    override fun getCount(): Int {
        return 10
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context?.getString(R.string.monday_short)
            1 -> context?.getString(R.string.tuesday_short)
            2 -> context?.getString(R.string.wednesday_short)
            3 -> context?.getString(R.string.thursday_short)
            4 -> context?.getString(R.string.friday_short)
            5 -> context?.getString(R.string.monday_short)
            6 -> context?.getString(R.string.tuesday_short)
            7 -> context?.getString(R.string.wednesday_short)
            8 -> context?.getString(R.string.thursday_short)
            9 -> context?.getString(R.string.friday_short)
            else -> null
        }
    }

}