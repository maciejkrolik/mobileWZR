package pl.expert.mobilewzr.ui.dayview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.day_view_item.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.dto.DayViewItem

class DayViewRecyclerAdapter(
    private val dataset: List<DayViewItem>
) : RecyclerView.Adapter<DayViewRecyclerAdapter.SubjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewRecyclerAdapter.SubjectsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.day_view_item, parent, false)
        return SubjectsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.startTimeTextView?.text = dataset[position].startTime
        holder.titleTextView?.text = dataset[position].title
    }

    override fun getItemCount() = dataset.size

    class SubjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var startTimeTextView: TextView? = null
        var titleTextView: TextView? = null

        init {
            startTimeTextView = itemView.start_time_day_view_text
            titleTextView = itemView.title_day_view_text
        }
    }
}