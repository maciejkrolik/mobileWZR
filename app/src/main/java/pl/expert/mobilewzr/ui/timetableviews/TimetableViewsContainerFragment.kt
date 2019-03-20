package pl.expert.mobilewzr.ui.timetableviews

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import dagger.android.support.AndroidSupportInjection
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

        timetableViewLocation = if (arguments?.getString("argGroupId").isNullOrEmpty())
            TimetableViewLocation.MY_TIMETABLE else TimetableViewLocation.SEARCH

        timetableViewType = TimetableViewType.DAY_VIEW

        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
        idOfAGroupSavedInDb = sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!

        if (!groupId.isEmpty()) {
            activity?.title =
                "${getString(R.string.group)}: $groupId (${CalendarUtils.getWeekType()})"

            when (timetableViewType) {
                TimetableViewType.WEEK_VIEW -> {
                    pagerAdapter = WeekViewPagerAdapter(context, timetableViewLocation, childFragmentManager)
                    binding.timetableViewViewPager.adapter = pagerAdapter
                    binding.timetableViewViewPager.currentItem = weekNumber
                }
                TimetableViewType.DAY_VIEW -> {
                    pagerAdapter = DayViewPagerAdapter(context, weekNumber, childFragmentManager)
                    binding.timetableViewViewPager.adapter = pagerAdapter
                    binding.timetableViewViewPager.currentItem = CalendarUtils.getDayOfWeek()
                }
            }

            binding.timetableViewContainerTabLayout.setupWithViewPager(binding.timetableViewViewPager)

            binding.timetableViewViewPager.visibility = View.VISIBLE
            binding.timetableViewContainerTabLayout.visibility = View.VISIBLE
        } else {
            activity?.title = getString(R.string.my_timetable)
            binding.timetableViewContainerTextInfo.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!groupId.isEmpty()) {
            when (timetableViewType) {
                TimetableViewType.WEEK_VIEW -> {
                    assignWeekViewViewModel()
                }
                TimetableViewType.DAY_VIEW -> {
                    assignDayViewViewModel()
                }
            }
        }
    }

    private fun assignWeekViewViewModel() {
        val weekViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(WeekViewViewModel::class.java)

        weekViewViewModel.setIdOfAGroupSavedInDb(idOfAGroupSavedInDb)
        weekViewViewModel.checkIfSubjectsLoaded(groupId)
    }

    private fun assignDayViewViewModel() {
        val dayViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(DayViewViewModel::class.java)

        dayViewViewModel.setIdOfAGroupSavedInDb(idOfAGroupSavedInDb)
        dayViewViewModel.checkIfSubjectsLoaded(groupId)
    }
}
