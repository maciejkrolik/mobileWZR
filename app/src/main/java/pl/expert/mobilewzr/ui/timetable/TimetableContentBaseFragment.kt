package pl.expert.mobilewzr.ui.timetable

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.setAttrsAndShow

abstract class TimetableContentBaseFragment : BaseInjectedFragment() {

    protected lateinit var timetableViewLocation: TimetableViewLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timetableViewLocation = TimetableViewLocation.getByValue(arguments?.getInt("argTimetableViewLocation")!!)
    }

    protected fun putMyGroupIdIntoSharedPref(groupId: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPref.edit().putString("prefIdOfAGroupSavedInDb", groupId).apply()
    }

    protected fun chooseView(groupId: String) {
        val alertDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it).apply {
                setTitle(R.string.choose_view)
                setItems(R.array.timetable_view_type_entries) { _, which ->
                    saveChosenViewTypeToSharedPrefs(which)
                    reloadView(groupId)
                }
            }
            builder.create()
        }
        alertDialog.setAttrsAndShow()
    }

    protected fun showToast(groupId: String) {
        Toast.makeText(
            context,
            "${getString(R.string.group)} $groupId ${getString(R.string.was_saved)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun saveChosenViewTypeToSharedPrefs(position: Int) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPrefs
            .edit()
            .putInt("prefTimetableViewType", position)
            .apply()
    }

    private fun reloadView(groupId: String) {
        when (timetableViewLocation) {
            TimetableViewLocation.MY_TIMETABLE -> {
                findNavController().navigate(R.id.action_my_timetable_view_fragment_self)
            }
            TimetableViewLocation.SEARCH -> {
                val args = Bundle()
                args.putString("argGroupId", groupId)
                findNavController().navigate(R.id.action_search_timetable_view_fragment_self, args)
            }
        }
    }

}