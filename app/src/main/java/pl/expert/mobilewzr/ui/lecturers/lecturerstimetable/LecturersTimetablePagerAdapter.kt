package pl.expert.mobilewzr.ui.lecturers.lecturerstimetable

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.expert.mobilewzr.R

class LecturersTimetablePagerAdapter(
    private val context: Context?,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        when (position) {
            0 -> args.putInt("argWeekNumber", 0)
            1 -> args.putInt("argWeekNumber", 1)
        }

        return LecturersTimetableContentFragment().apply {
            arguments = args
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context?.getString(R.string.weekA)
            1 -> context?.getString(R.string.weekB)
            else -> null
        }
    }

}