package pl.expert.mobilewzr.ui.newsview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.news_item.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.News

class NewsRecyclerAdapter(
    private val dataset: List<News>
) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.newsTitleTextView?.text = dataset[position].title
        holder.newsContentTextView?.text = dataset[position].content
    }

    override fun getItemCount() = dataset.size

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsTitleTextView: TextView? = null
        var newsContentTextView: TextView? = null

        init {
            newsTitleTextView = itemView.news_title_text
            newsContentTextView = itemView.news_content_text
        }
    }
}