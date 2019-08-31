package pl.expert.mobilewzr.ui.news

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.News
import pl.expert.mobilewzr.databinding.FragmentNewsBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.NetworkUtils

class NewsFragment : BaseInjectedFragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    private var isNetworkAvailable = false

    private val news: MutableList<News> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        toolbar.toolbarTitle.text = getString(R.string.news)

        isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(news)

        newsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }

        newsButton.setOnClickListener {
            isNetworkAvailable = NetworkUtils.isNetworkAvailable(requireContext())
            if (isNetworkAvailable) {
                hideInternetErrorNewsViewItems()
                showMainNewsViewItems()
                getViewModel()
                getNewsAndObserve()
            }
        }

        if (isNetworkAvailable) {
            getViewModel()
            getNewsAndObserve()
        } else {
            hideMainNewsViewItems()
            showInternetErrorNewsViewItems()
        }
    }

    private fun getViewModel() {
        newsViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)
    }

    private fun getNewsAndObserve() {
        newsViewModel.getNews().observe(viewLifecycleOwner,
                Observer<List<News>> { news ->
                    if (news != null && news.isNotEmpty()) {
                        this.news.addAll(news)
                        newsRecyclerView.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
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
