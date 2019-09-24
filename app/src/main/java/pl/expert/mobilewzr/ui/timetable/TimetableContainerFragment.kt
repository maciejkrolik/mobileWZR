package pl.expert.mobilewzr.ui.timetable

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_timetable_views_container.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.ui.timetable.calendarview.CalendarViewContentFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewContainerFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewViewModel
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewContainerFragment
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewViewModel

class TimetableContainerFragment : BaseInjectedFragment() {

    private lateinit var groupId: String
    private lateinit var idOfAGroupSavedInDb: String
    private lateinit var timetableViewLocation: TimetableViewLocation
    private lateinit var timetableViewType: TimetableViewType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timetable_views_container, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()

        if (timetableViewType == TimetableViewType.CALENDAR_VIEW) {
            inflater.inflate(R.menu.calendar_view_menu, menu)
        } else
            when (timetableViewLocation) {
                TimetableViewLocation.MY_TIMETABLE -> inflater.inflate(R.menu.saved_timetable_menu, menu)
                TimetableViewLocation.SEARCH -> inflater.inflate(R.menu.downloaded_timetable_menu, menu)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromSharedPrefs()

        if (groupId.isNotEmpty()) {
            setupTimetableView()
            assignViewModel()
        } else {
            toolbar.toolbarTitle.text = getString(R.string.my_timetable)
            timetableContent.visibility = View.GONE
            timetableViewContainerTextInfo.visibility = View.VISIBLE
        }
    }

    private fun getDataFromSharedPrefs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        idOfAGroupSavedInDb = sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!

        timetableViewType =
            TimetableViewType.getByValue(sharedPref.getInt("prefTimetableViewType", TimetableViewType.DAY_VIEW.value))

        timetableViewLocation = if (arguments?.getString("argGroupId").isNullOrEmpty())
            TimetableViewLocation.MY_TIMETABLE
        else
            TimetableViewLocation.SEARCH

        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
    }

    private fun setupTimetableView() {
        val args = Bundle()
        args.putInt("argTimetableViewLocation", timetableViewLocation.value)
        args.putString("argGroupId", groupId)

        var currentFragment: Fragment? = childFragmentManager.findFragmentByTag("timetable_view")
        if (currentFragment == null) {
            when (timetableViewType) {
                TimetableViewType.WEEK_VIEW -> {
                    currentFragment = WeekViewContainerFragment()
                }
                TimetableViewType.DAY_VIEW -> {
                    currentFragment = DayViewContainerFragment()
                }
                TimetableViewType.CALENDAR_VIEW -> {
                    currentFragment = CalendarViewContentFragment()
                }
            }
        }

        currentFragment.arguments = args
        childFragmentManager
            .beginTransaction()
            .replace(R.id.timetableContent, currentFragment, "timetable_view")
            .commit()
    }

    private fun assignViewModel() {
        assignWeekViewViewModel()
        assignDayViewViewModel()
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
