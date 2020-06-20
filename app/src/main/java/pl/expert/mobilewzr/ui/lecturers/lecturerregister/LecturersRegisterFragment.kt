package pl.expert.mobilewzr.ui.lecturers.lecturerregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_lecturers_register.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentLecturersRegisterBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.ResourceState

class LecturersRegisterFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LecturersRegisterViewModel::class.java)
    }

    private lateinit var binding: FragmentLecturersRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLecturersRegisterBinding.inflate(inflater, container, false)
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
        toolbar.toolbarTitle.text = getString(R.string.register)
    }

    private fun setOnClickListeners() {
        registerButton.setOnClickListener {
            viewModel.register(
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
                tokenEditText.text.toString()
            )
        }
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun observeData() {
        viewModel.registerState.observe(viewLifecycleOwner, Observer { registerState ->
            when (registerState) {
                is ResourceState.Success -> {
                    Toast.makeText(requireContext(), R.string.register_successfully, Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                is ResourceState.Error -> {
                    Toast.makeText(requireContext(), registerState.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}