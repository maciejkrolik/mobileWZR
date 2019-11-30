package pl.expert.mobilewzr.ui.timetable

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_container_timetable_views.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.ui.timetable.calendarview.CalendarViewContentFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewContainerFragment
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewContainerFragment
import pl.expert.mobilewzr.util.isFullTime

class TimetableContainerFragment : BaseInjectedFragment() {

    private lateinit var groupId: String
    private lateinit var myGroupId: String
    private lateinit var timetableViewLocation: TimetableViewLocation
    private lateinit var timetableViewType: TimetableViewType
    private lateinit var timetableType: TimetableType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDataFromSharedPrefs()
        setTimetableType()
        return inflater.inflate(R.layout.fragment_container_timetable_views, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (groupId.isNotEmpty()) {
            if (timetableViewType == TimetableViewType.CALENDAR_VIEW) {
                inflater.inflate(R.menu.calendar_view_menu, menu)
                if (timetableViewLocation == TimetableViewLocation.SEARCH) {
                    menu.add(Menu.NONE, R.id.set_as_my_timetable, Menu.NONE, R.string.set_as_my_timetable)
                }
                if (timetableType == TimetableType.FULL_TIME) {
                    menu.add(Menu.NONE, R.id.choose_view, Menu.NONE, R.string.choose_view)
                }
            } else
                when (timetableViewLocation) {
                    TimetableViewLocation.MY_TIMETABLE -> inflater.inflate(R.menu.my_timetable_menu, menu)
                    TimetableViewLocation.SEARCH -> inflater.inflate(R.menu.search_timetable_menu, menu)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        myGroupId = sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!

        timetableViewType =
            TimetableViewType.getByValue(sharedPref.getInt("prefTimetableViewType", TimetableViewType.DAY_VIEW.value))

        timetableViewLocation = if (arguments?.getString("argGroupId").isNullOrEmpty())
            TimetableViewLocation.MY_TIMETABLE
        else
            TimetableViewLocation.SEARCH

        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
    }

    private fun setTimetableType() {
        if (groupId.isFullTime()) {
            timetableType = TimetableType.FULL_TIME
        } else {
            timetableType = TimetableType.PART_TIME
            timetableViewType = TimetableViewType.CALENDAR_VIEW
        }
    }

    private fun setupTimetableView() {
        val args = Bundle()
        args.putInt("argTimetableViewLocation", timetableViewLocation.value)
        args.putString("argGroupId", groupId)

        var currentFragment: Fragment? = childFragmentManager.findFragmentByTag("timetable_view")
        if (currentFragment == null && timetableType != TimetableType.PART_TIME) {
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
        } else if (currentFragment == null) {
            currentFragment = CalendarViewContentFragment()
        }

        currentFragment.arguments = args
        childFragmentManager
            .beginTransaction()
            .replace(R.id.timetableContent, currentFragment, "timetable_view")
            .commit()
    }

    private fun assignViewModel() {
        val viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(TimetableViewModel::class.java)
        viewModel.init(groupId, timetableViewLocation)
    }

}
