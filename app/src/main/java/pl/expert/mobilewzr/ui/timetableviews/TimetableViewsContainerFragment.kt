package pl.expert.mobilewzr.ui.timetableviews

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentTimetableViewsContainerBinding
import pl.expert.mobilewzr.ui.timetableviews.dayview.DayViewPagerAdapter
import pl.expert.mobilewzr.ui.timetableviews.dayview.DayViewViewModel
import pl.expert.mobilewzr.ui.timetableviews.weekview.WeekViewPagerAdapter
import pl.expert.mobilewzr.ui.timetableviews.weekview.WeekViewViewModel
import pl.expert.mobilewzr.util.CalendarUtils
import javax.inject.Inject

class TimetableViewsContainerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentTimetableViewsContainerBinding
    private lateinit var groupId: String
    private lateinit var idOfAGroupSavedInDb: String
    private lateinit var timetableViewLocation: TimetableViewLocation
    private lateinit var timetableViewType: TimetableViewType
    private lateinit var pagerAdapter: PagerAdapter
    private var weekNumber: Int = 0

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTimetableViewsContainerBinding.inflate(inflater, container, false)

        weekNumber = CalendarUtils.getWeekNumber()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        idOfAGroupSavedInDb = sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        timetableViewType =
            TimetableViewType.getByValue(
                sharedPref.getString(
                    "prefTimetableViewType",
                    TimetableViewType.DAY_VIEW.value.toString()
                ).toInt()
            )

        timetableViewLocation = if (arguments?.getString("argGroupId").isNullOrEmpty())
            TimetableViewLocation.MY_TIMETABLE else TimetableViewLocation.SEARCH
        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!

        if (!groupId.isEmpty()) {
            prepareTimetableView()
            prepareTimetableViewTabLayout()
            binding.timetableViewContainerTabLayout.setupWithViewPager(binding.timetableViewViewPager)

            binding.timetableViewViewPager.visibility = View.VISIBLE
            binding.timetableViewContainerTabLayout.visibility = View.VISIBLE
        } else {
            (activity as AppCompatActivity).toolbar.toolbarTitle.text = getString(R.string.my_timetable)
            binding.timetableViewContainerTextInfo.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!groupId.isEmpty()) {
            assignTimetableViewViewModel()
        }
    }

    private fun prepareTimetableView() {
        when (timetableViewType) {
            TimetableViewType.WEEK_VIEW -> {
                (activity as AppCompatActivity).toolbar.toolbarTitle.text = "${getString(R.string.group)}: $groupId"
                prepareWeekViewPagerAdapter()
            }
            TimetableViewType.DAY_VIEW -> {
                (activity as AppCompatActivity).toolbar.toolbarTitle.text =
                    "${getString(R.string.group)}: $groupId (${getString(R.string.week)} ${CalendarUtils.getWeekType(
                        weekNumber
                    )})"
                prepareDayViewPagerAdapter()
            }
        }
    }

    private fun prepareWeekViewPagerAdapter() {
        pagerAdapter = WeekViewPagerAdapter(context, timetableViewLocation, childFragmentManager)
        binding.timetableViewViewPager.adapter = pagerAdapter
        binding.timetableViewViewPager.currentItem = weekNumber
    }

    private fun prepareDayViewPagerAdapter() {
        pagerAdapter = DayViewPagerAdapter(context, timetableViewLocation, childFragmentManager)
        binding.timetableViewViewPager.adapter = pagerAdapter
        setDayViewViewPagerCurrentItem()
        assignOnPageChangeListener()
    }

    private fun setDayViewViewPagerCurrentItem() {
        binding.timetableViewViewPager.currentItem =
            if (weekNumber == 0) CalendarUtils.getDayOfWeek() else CalendarUtils.getDayOfWeek() + 5
    }

    private fun assignOnPageChangeListener() {
        binding.timetableViewViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(dayNumber: Int) {
                when (dayNumber) {
                    0, 1, 2, 3, 4 -> (activity as AppCompatActivity).toolbar.toolbarTitle.text =
                        "${getString(R.string.group)}: $groupId (${getString(R.string.week)} A)"
                    5, 6, 7, 8, 9 -> (activity as AppCompatActivity).toolbar.toolbarTitle.text =
                        "${getString(R.string.group)}: $groupId (${getString(R.string.week)} B)"
                }
            }
        })
    }

    private fun prepareTimetableViewTabLayout() {
        if (timetableViewType == TimetableViewType.DAY_VIEW) {
            binding.timetableViewContainerTabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun assignTimetableViewViewModel() {
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
