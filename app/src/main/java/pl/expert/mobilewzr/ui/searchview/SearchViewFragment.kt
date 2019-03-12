package pl.expert.mobilewzr.ui.searchview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentSearchViewBinding
import javax.inject.Inject

class SearchViewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentSearchViewBinding
    private lateinit var searchViewViewModel: SearchViewViewModel
    private lateinit var spinnerAdapter: ArrayAdapter<String>

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

        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.searchViewSpinner.adapter = spinnerAdapter

        binding.searchViewButton.setOnClickListener { view ->
            val args = Bundle().apply {
                putString("argGroupId", binding.searchViewSpinner.selectedItem.toString())
            }
            searchViewViewModel.setGroupIdIndex(binding.searchViewSpinner.selectedItemId.toInt())
            Navigation.findNavController(view)
                .navigate(R.id.action_search_view_fragment_to_search_week_view_fragment, args)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(SearchViewViewModel::class.java)

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
}