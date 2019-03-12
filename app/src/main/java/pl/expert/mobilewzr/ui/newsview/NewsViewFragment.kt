package pl.expert.mobilewzr.ui.newsview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.News
import pl.expert.mobilewzr.databinding.FragmentNewsViewBinding
import javax.inject.Inject

class NewsViewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentNewsViewBinding

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

        activity?.title = getString(R.string.news)

        val recyclerAdapter = NewsRecyclerAdapter(news)

        binding.newsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val newsViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(NewsViewViewModel::class.java)

        newsViewViewModel.getNews().observe(viewLifecycleOwner,
            Observer<List<News>> { listOfNews ->
                if (listOfNews != null) {
                    news.addAll(listOfNews)
                }
                binding.newsProgressBar.visibility = View.GONE
                binding.newsRecyclerView.visibility = View.VISIBLE
            })
    }
}
