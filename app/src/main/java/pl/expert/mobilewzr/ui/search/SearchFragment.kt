package pl.expert.mobilewzr.ui.search

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentSearchBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.NetworkUtils

class SearchFragment : BaseInjectedFragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private var isNetworkAvailable = false

    private val groups: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).toolbar.toolbarTitle.text = getString(R.string.search)

        isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.searchViewSpinner.adapter = spinnerAdapter

        binding.searchViewButton.setOnClickListener {
            val args = Bundle().apply {
                putString("argGroupId", binding.searchViewSpinner.selectedItem.toString())
            }
            searchViewModel.setGroupIdIndex(binding.searchViewSpinner.selectedItemId.toInt())
            findNavController().navigate(
                R.id.action_search_view_fragment_to_search_timetable_view_fragment,
                args
            )
        }

        binding.internetErrorSearchViewButton.setOnClickListener {
            isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())
            if (isNetworkAvailable) {
                hideInternetErrorSearchViewItems()
                showMainSearchViewItems()
                getViewModel()
                getGroupsAndObserveThem()
            }
        }

        if (isNetworkAvailable) {
            getViewModel()
            getGroupsAndObserveThem()
        } else {
            hideMainSearchViewItems()
            showInternetErrorSearchViewItems()
        }
    }

    private fun getViewModel() {
        searchViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(SearchViewModel::class.java)
    }

    private fun getGroupsAndObserveThem() {
        searchViewModel.getGroups().observe(viewLifecycleOwner,
            Observer { groups ->
                if (groups != null && groups.isNotEmpty()) {
                    this.groups.clear()
                    this.groups.addAll(groups)
                    spinnerAdapter.notifyDataSetChanged()
                    binding.searchViewSpinner.setSelection(searchViewModel.getGroupIdIndex())
                } else {
                    binding.searchViewButton.isEnabled = false
                }
                binding.searchViewButton.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.GONE
            })
    }

    private fun showMainSearchViewItems() {
        binding.searchViewSpinner.visibility = View.VISIBLE
        binding.searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideMainSearchViewItems() {
        binding.searchViewSpinner.visibility = View.GONE
        binding.searchViewButton.visibility = View.GONE
        binding.searchProgressBar.visibility = View.GONE
    }

    private fun showInternetErrorSearchViewItems() {
        binding.internetErrorSearchViewText.visibility = View.VISIBLE
        binding.internetErrorSearchViewButton.visibility = View.VISIBLE
    }

    private fun hideInternetErrorSearchViewItems() {
        binding.internetErrorSearchViewText.visibility = View.GONE
        binding.internetErrorSearchViewButton.visibility = View.GONE
    }

}