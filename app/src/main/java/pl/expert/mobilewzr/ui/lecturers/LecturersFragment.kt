package pl.expert.mobilewzr.ui.lecturers

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_lecturers.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Lecturer
import pl.expert.mobilewzr.databinding.FragmentLecturersBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.ResourceState
import pl.expert.mobilewzr.util.addScrollToStartAfterSubmitListener

class LecturersFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(LecturersViewModel::class.java)
    }

    private lateinit var binding: FragmentLecturersBinding
    private lateinit var recyclerAdapter: LecturersRecyclerAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLecturersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lecturers_menu, menu)
        setupSearchView(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        setupRecyclerView()
        setupViewModel()
        observeData()
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = getString(R.string.lecturers)
    }

    private fun setupRecyclerView() {
        recyclerAdapter = LecturersRecyclerAdapter { lecturer -> onLecturerClick(lecturer) }
        recyclerAdapter.addScrollToStartAfterSubmitListener(lecturersRecyclerView)
        lecturersRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun setupSearchView(menu: Menu) {
        searchView = menu.findItem(R.id.search).actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                recyclerAdapter.filterList(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                recyclerAdapter.filterList(query)
                return true
            }
        })
    }

    private fun observeData() {
        viewModel.lecturersState.observe(viewLifecycleOwner, { lecturersState ->
            when (lecturersState) {
                is ResourceState.Success -> {
                    recyclerAdapter.setItems(lecturersState.data)
                }
            }
        })
    }

    private fun onLecturerClick(lecturer: Lecturer) {
        findNavController().navigate(
            LecturersFragmentDirections.actionLecturersFragmentToLecturersTimetableFragment(lecturer.name)
        )
        toolbar.collapseActionView()
    }

}
