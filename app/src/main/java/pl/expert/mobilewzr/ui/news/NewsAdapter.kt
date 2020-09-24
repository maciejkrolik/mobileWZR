package pl.expert.mobilewzr.ui.news

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_news.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.News

class NewsAdapter(
    private val dataSet: List<News>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.newsTitleTextView?.text = dataSet[position].title
        holder.newsContentTextView?.text = HtmlCompat.fromHtml(
            dataSet[position].content,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).trim()
        Linkify.addLinks(holder.newsContentTextView!!, Linkify.ALL)
    }

    override fun getItemCount() = dataSet.size

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsTitleTextView: TextView? = null
        var newsContentTextView: TextView? = null

        init {
            newsTitleTextView = itemView.news_title_text
            newsContentTextView = itemView.news_content_text
        }
    }

}