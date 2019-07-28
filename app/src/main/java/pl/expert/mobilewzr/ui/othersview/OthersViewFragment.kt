package pl.expert.mobilewzr.ui.othersview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_others_view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R

class OthersViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).toolbar.toolbarTitle.text = getString(R.string.others)
        return inflater.inflate(R.layout.fragment_others_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        settingsCardView.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_others_view_fragment_to_settings_view_fragment)
        }
    }
}
