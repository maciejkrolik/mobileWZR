package pl.expert.mobilewzr.ui.timetable

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.BaseInjectedFragment

abstract class TimetableContentBaseFragment : BaseInjectedFragment() {

    protected lateinit var timetableViewLocation: TimetableViewLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        when (timetableViewLocation) {
            TimetableViewLocation.MY_TIMETABLE -> inflater?.inflate(R.menu.saved_timetable_menu, menu)
            TimetableViewLocation.SEARCH -> inflater?.inflate(R.menu.downloaded_timetable_menu, menu)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timetableViewLocation = TimetableViewLocation.getByValue(arguments?.getInt("argTimetableViewLocation")!!)

    }

    protected fun putIdOfAGroupSavedInDbIntoSharedPref(groupId: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPref.edit().putString("prefIdOfAGroupSavedInDb", groupId).apply()
    }

    protected fun showToast(groupId: String) {
        Toast.makeText(
            context,
            "${getString(R.string.group)} $groupId ${getString(R.string.was_saved)}",
            Toast.LENGTH_SHORT
        ).show()
    }

}