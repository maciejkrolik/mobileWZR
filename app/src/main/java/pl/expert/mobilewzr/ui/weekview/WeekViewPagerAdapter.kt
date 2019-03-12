package pl.expert.mobilewzr.ui.weekview

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import pl.expert.mobilewzr.R

class WeekViewPagerAdapter(
    private val context: Context?,
    private val weekViewLocation: WeekViewLocation,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        when (position) {
            0 -> args.putInt("argWeekNumber", 0)
            1 -> args.putInt("argWeekNumber", 1)
        }
        args.putInt("argWeekViewLocation", weekViewLocation.value)

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