package pl.expert.mobilewzr.ui.searchview

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentSearchViewBinding
import pl.expert.mobilewzr.util.NetworkUtils
import javax.inject.Inject

class SearchViewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentSearchViewBinding
    private lateinit var searchViewViewModel: SearchViewViewModel
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private var isNetworkAvailable = false

    private val groups: MutableList<String> = mutableListOf()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchViewBinding.inflate(inflater, container, false)

        activity?.title = getString(R.string.search)

        isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())

        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.searchViewSpinner.adapter = spinnerAdapter

        binding.searchViewButton.setOnClickListener { view ->
            val args = Bundle().apply {
                putString("argGroupId", binding.searchViewSpinner.selectedItem.toString())
            }
            searchViewViewModel.setGroupIdIndex(binding.searchViewSpinner.selectedItemId.toInt())
            Navigation.findNavController(view)
                .navigate(R.id.action_search_view_fragment_to_search_timetable_view_fragment, args)
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

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isNetworkAvailable) {
            getViewModel()
            getGroupsAndObserveThem()
        } else {
            hideMainSearchViewItems()
            showInternetErrorSearchViewItems()
        }
    }

    private fun getViewModel() {
        searchViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(SearchViewViewModel::class.java)
    }

    private fun getGroupsAndObserveThem() {
        searchViewViewModel.getGroups().observe(viewLifecycleOwner,
            Observer { groups ->
                if (groups != null) {
                    this.groups.clear()
                    this.groups.addAll(groups)
                }
                binding.searchProgressBar.visibility = View.GONE
                binding.searchViewButton.visibility = View.VISIBLE
                spinnerAdapter.notifyDataSetChanged()
                binding.searchViewSpinner.setSelection(searchViewViewModel.getGroupIdIndex())
            })
    }

    private fun showMainSearchViewItems() {
        binding.searchViewText.visibility = View.VISIBLE
        binding.searchViewSpinner.visibility = View.VISIBLE
        binding.searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideMainSearchViewItems() {
        binding.searchViewText.visibility = View.GONE
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