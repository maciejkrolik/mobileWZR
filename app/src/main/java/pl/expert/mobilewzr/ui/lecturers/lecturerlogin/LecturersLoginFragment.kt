package pl.expert.mobilewzr.ui.lecturers.lecturerlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_lecturers_login.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentLecturersLoginBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.ResourceState

class LecturersLoginFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(LecturersLoginViewModel::class.java)
    }

    private lateinit var binding: FragmentLecturersLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLecturersLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        setOnClickListeners()
        setupDataBinding()
        observeData()
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = getString(R.string.login_for_lecturers)
    }

    private fun setOnClickListeners() {
        loginButton.setOnClickListener {
            viewModel.login(emailEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun observeData() {
        viewModel.loginState.observe(viewLifecycleOwner, Observer { loginState ->
            when (loginState) {
                is ResourceState.Success -> {
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                }
                is ResourceState.Error -> {
                    Toast.makeText(requireContext(), loginState.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}