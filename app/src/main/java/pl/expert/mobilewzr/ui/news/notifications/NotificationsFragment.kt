package pl.expert.mobilewzr.ui.news.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentNotificationsBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment

class NotificationsFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NotificationsViewModel::class.java)
    }

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = getString(R.string.notifications)
    }

}