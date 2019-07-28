package pl.expert.mobilewzr.ui.newsview

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.News
import pl.expert.mobilewzr.databinding.FragmentNewsViewBinding
import pl.expert.mobilewzr.util.NetworkUtils
import javax.inject.Inject

class NewsViewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentNewsViewBinding
    private lateinit var newsViewViewModel: NewsViewViewModel
    private lateinit var recyclerAdapter: NewsRecyclerAdapter

    private var isNetworkAvailable = false

    private val news: MutableList<News> = mutableListOf()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewsViewBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).toolbar.toolbarTitle.text = getString(R.string.news)

        isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())

        recyclerAdapter = NewsRecyclerAdapter(news)

        binding.newsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        binding.internetErrorNewsViewButton.setOnClickListener {
            isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())
            if (isNetworkAvailable) {
                hideInternetErrorNewsViewItems()
                showMainNewsViewItems()
                getViewModel()
                getNewsAndObserve()
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
            getNewsAndObserve()
        } else {
            hideMainNewsViewItems()
            showInternetErrorNewsViewItems()
        }
    }

    private fun getViewModel() {
        newsViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(NewsViewViewModel::class.java)
    }

    private fun getNewsAndObserve() {
        newsViewViewModel.getNews().observe(viewLifecycleOwner,
            Observer<List<News>> { news ->
                if (news != null && !news.isEmpty()) {
                    this.news.addAll(news)
                    binding.newsRecyclerView.visibility = View.VISIBLE
                    recyclerAdapter.notifyDataSetChanged()
                } else {
                    binding.internetErrorNewsViewText.visibility = View.VISIBLE
                    binding.internetErrorNewsViewText.text = getString(R.string.server_error)
                }
                binding.newsProgressBar.visibility = View.GONE
            })
    }

    private fun showMainNewsViewItems() {
        binding.newsRecyclerView.visibility = View.VISIBLE
        binding.newsProgressBar.visibility = View.VISIBLE
    }

    private fun hideMainNewsViewItems() {
        binding.newsRecyclerView.visibility = View.GONE
        binding.newsProgressBar.visibility = View.GONE
    }

    private fun showInternetErrorNewsViewItems() {
        binding.internetErrorNewsViewText.visibility = View.VISIBLE
        binding.internetErrorNewsViewButton.visibility = View.VISIBLE
    }

    private fun hideInternetErrorNewsViewItems() {
        binding.internetErrorNewsViewText.visibility = View.GONE
        binding.internetErrorNewsViewButton.visibility = View.GONE
    }
}
