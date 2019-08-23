package pl.expert.mobilewzr.ui.timetable

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R

abstract class TimetableContentBaseFragment : Fragment() {

    protected lateinit var timetableViewLocation: TimetableViewLocation

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        timetableViewLocation = TimetableViewLocation.getByValue(arguments?.getInt("argTimetableViewLocation")!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        when (timetableViewLocation) {
            TimetableViewLocation.MY_TIMETABLE -> inflater?.inflate(R.menu.saved_timetable_menu, menu)
            TimetableViewLocation.SEARCH -> inflater?.inflate(R.menu.downloaded_timetable_menu, menu)
        }
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