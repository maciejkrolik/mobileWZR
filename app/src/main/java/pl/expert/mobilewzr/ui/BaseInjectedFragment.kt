package pl.expert.mobilewzr.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

abstract class BaseInjectedFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val toolbar: Toolbar by lazy { (activity as AppCompatActivity).toolbar }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.additionalToolbarBtn.isVisible = false
    }

}