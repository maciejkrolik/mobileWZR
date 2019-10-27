package pl.expert.mobilewzr.ui.lecturers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_lecturers.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Lecturer
import pl.expert.mobilewzr.databinding.FragmentLecturersBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.ResourceState

class LecturersFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(LecturersViewModel::class.java)
    }

    private lateinit var binding: FragmentLecturersBinding
    private lateinit var recyclerAdapter: LecturersAdapter

    private val lecturers = mutableListOf<Lecturer>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLecturersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        setupRecyclerView()
        setupSearch()
        setupViewModel()
        observeData()
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = getString(R.string.lecturers)
    }

    private fun setupRecyclerView() {
        recyclerAdapter = LecturersAdapter(lecturers)
        lecturersRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    private fun setupSearch() {
        lecturersEditText.doAfterTextChanged { input ->
            recyclerAdapter.filterList(input.toString())
        }
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun observeData() {
        viewModel.lecturersState.observe(viewLifecycleOwner, Observer { lecturersState ->
            when (lecturersState) {
                is ResourceState.Success -> {
                    recyclerAdapter.setItems(lecturersState.data ?: emptyList())
                    recyclerAdapter.refreshFullList(lecturersState.data ?: emptyList())
                }
            }
        })
    }

}
