package pl.expert.mobilewzr.ui.newsview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.expert.mobilewzr.R

class NewsViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.news)
        return inflater.inflate(R.layout.fragment_news_view, container, false)
    }
}
