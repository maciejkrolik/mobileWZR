package pl.expert.mobilewzr.ui.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_timetable_views_container.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentTimetableViewsContainerBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewPagerAdapter
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewViewModel
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewPagerAdapter
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewViewModel
import pl.expert.mobilewzr.util.CalendarUtils

class TimetableContainerFragment : BaseInjectedFragment() {

    private lateinit var binding: FragmentTimetableViewsContainerBinding
    private lateinit var groupId: String
    private lateinit var idOfAGroupSavedInDb: String
    private lateinit var timetableViewLocation: TimetableViewLocation
    private lateinit var timetableViewType: TimetableViewType
    private lateinit var pagerAdapter: PagerAdapter
    private var weekNumber: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimetableViewsContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekNumber = CalendarUtils.getWeekNumber()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        idOfAGroupSavedInDb = sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        timetableViewType =
            TimetableViewType.getByValue(
                sharedPref.getString(
                    "prefTimetableViewType",
                    TimetableViewType.DAY_VIEW.value.toString()
                )!!.toInt()
            )

        timetableViewLocation = if (arguments?.getString("argGroupId").isNullOrEmpty())
            TimetableViewLocation.MY_TIMETABLE else TimetableViewLocation.SEARCH
        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString(
            "prefIdOfAGroupSavedInDb",
            ""
        )!!

        if (groupId.isNotEmpty()) {
            setupTimetableView()
            setupTabLayout()
            tabLayout.setupWithViewPager(viewPager)
            assignViewModel()
            viewPager.visibility = View.VISIBLE
            tabLayout.visibility = View.VISIBLE
        } else {
            toolbar.toolbarTitle.text = getString(R.string.my_timetable)
            timetableViewContainerTextInfo.visibility = View.VISIBLE
        }
    }

    private fun setupTimetableView() {
        when (timetableViewType) {
            TimetableViewType.WEEK_VIEW -> {
                (activity as AppCompatActivity).toolbar.toolbarTitle.text =
                    getString(R.string.timetable_title_base, groupId)
                setupWeekViewPagerAdapter()
            }
            TimetableViewType.DAY_VIEW -> {
                (activity as AppCompatActivity).toolbar.toolbarTitle.text =
                    getString(
                        R.string.timetable_title_with_week,
                        groupId,
                        CalendarUtils.getWeekType(weekNumber)
                    )
                setupDayViewPagerAdapter()
            }
        }
    }

    private fun setupWeekViewPagerAdapter() {
        pagerAdapter = WeekViewPagerAdapter(context, timetableViewLocation, childFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = weekNumber
    }

    private fun setupDayViewPagerAdapter() {
        pagerAdapter = DayViewPagerAdapter(context, timetableViewLocation, childFragmentManager)
        viewPager.adapter = pagerAdapter
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
                    0, 1, 2, 3, 4 -> toolbar.toolbarTitle.text =
                        getString(R.string.timetable_title_week_a, groupId)
                    5, 6, 7, 8, 9 -> toolbar.toolbarTitle.text =
                        getString(R.string.timetable_title_week_b, groupId)
                }
            }
        })
    }

    private fun setupTabLayout() {
        if (timetableViewType == TimetableViewType.DAY_VIEW) {
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun assignViewModel() {
        when (timetableViewType) {
            TimetableViewType.WEEK_VIEW -> {
                assignWeekViewViewModel()
            }
            TimetableViewType.DAY_VIEW -> {
                assignDayViewViewModel()
            }
        }
    }

    private fun assignWeekViewViewModel() {
        val weekViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(WeekViewViewModel::class.java)

        weekViewViewModel.setIdOfAGroupSavedInDb(idOfAGroupSavedInDb)
        weekViewViewModel.checkIfSubjectsLoaded(groupId, timetableViewLocation)
    }

    private fun assignDayViewViewModel() {
        val dayViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(DayViewViewModel::class.java)

        dayViewViewModel.setIdOfAGroupSavedInDb(idOfAGroupSavedInDb)
        dayViewViewModel.checkIfSubjectsLoaded(groupId, timetableViewLocation)
    }
}
