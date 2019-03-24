package pl.expert.mobilewzr.ui.othersview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import pl.expert.mobilewzr.R

class OthersViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.others)
        return inflater.inflate(R.layout.fragment_others_view, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.others_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> {
                Navigation.findNavController(view!!)
                    .navigate(R.id.action_others_view_fragment_to_settings_view_fragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
