package pl.expert.mobilewzr.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

abstract class BaseInjectedFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val toolbar: Toolbar by lazy { (activity as AppCompatActivity).toolbar }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

}