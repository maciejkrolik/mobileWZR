package pl.expert.mobilewzr.ui.news

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.News
import pl.expert.mobilewzr.databinding.FragmentNewsBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.NetworkUtils

class NewsFragment : BaseInjectedFragment() {

    private val viewModel by lazy { ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java) }

    private lateinit var binding: FragmentNewsBinding
    private lateinit var recyclerAdapter: NewsAdapter

    private var isNetworkAvailable = false

    private val news: MutableList<News> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())

        setTitle()
        setupRecyclerView()
        setOnClickListeners()

        if (isNetworkAvailable) {
            getNewsAndObserve()
        } else {
            hideMainNewsViewItems()
            showInternetErrorNewsViewItems()
        }
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = getString(R.string.news)
    }

    private fun setupRecyclerView() {
        recyclerAdapter = NewsAdapter(news)
        newsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())
            if (isNetworkAvailable) {
                hideInternetErrorNewsViewItems()
                viewModel.reloadNews()
            } else {
                swipeRefreshLayout.isRefreshing = false
                hideMainNewsViewItems()
                showInternetErrorNewsViewItems()
            }
        }
    }

    private fun setOnClickListeners() {
        newsButton.setOnClickListener {
            isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())
            if (isNetworkAvailable) {
                hideInternetErrorNewsViewItems()
                showMainNewsViewItems()
                getNewsAndObserve()
            }
        }
    }

    private fun getNewsAndObserve() {
        viewModel.getNews().observe(viewLifecycleOwner, { news ->
            if (news != null && news.isNotEmpty()) {
                this.news.clear()
                this.news.addAll(news)
                newsRecyclerView.visibility = View.VISIBLE
                recyclerAdapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            } else {
                newsTextView.text = getString(R.string.no_news_available)
                newsTextView.visibility = View.VISIBLE
            }
            newsProgressBar.visibility = View.GONE
        })
    }

    private fun showMainNewsViewItems() {
        newsRecyclerView.visibility = View.VISIBLE
        newsProgressBar.visibility = View.VISIBLE
    }

    private fun hideMainNewsViewItems() {
        newsRecyclerView.visibility = View.GONE
        newsProgressBar.visibility = View.GONE
    }

    private fun showInternetErrorNewsViewItems() {
        newsTextView.visibility = View.VISIBLE
        newsButton.visibility = View.VISIBLE
    }

    private fun hideInternetErrorNewsViewItems() {
        newsTextView.visibility = View.GONE
        newsButton.visibility = View.GONE
    }

}
