package pl.expert.mobilewzr.ui.timetableviews.dayview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.day_view_item.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.dto.DayViewItem

class DayViewRecyclerAdapter(
    private val dataset: List<DayViewItem>,
    private val onSubjectListener: OnSubjectListener
) : RecyclerView.Adapter<DayViewRecyclerAdapter.SubjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewRecyclerAdapter.SubjectsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.day_view_item, parent, false)
        return SubjectsViewHolder(itemView, onSubjectListener)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.startTimeTextView?.text = dataset[position].startTime
        holder.endTimeTextView?.text = dataset[position].endTime
        holder.titleTextView?.text = dataset[position].title
        holder.locationWithDescriptionTextView?.text = dataset[position].locationWithDescription
    }

    override fun getItemCount() = dataset.size

    class SubjectsViewHolder(itemView: View, onSubjectListener: OnSubjectListener) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener {

        var startTimeTextView: TextView? = null
        var endTimeTextView: TextView? = null
        var titleTextView: TextView? = null
        var locationWithDescriptionTextView: TextView? = null
        var onSubjectListener: OnSubjectListener? = null

        init {
            startTimeTextView = itemView.start_time_day_view_text
            endTimeTextView = itemView.end_time_day_view_text
            titleTextView = itemView.title_day_view_text
            locationWithDescriptionTextView = itemView.location_description_day_view_text
            this.onSubjectListener = onSubjectListener

            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(view: View?): Boolean {
            onSubjectListener?.onSubjectLongClick(adapterPosition)
            return true
        }
    }

    interface OnSubjectListener {
        fun onSubjectLongClick(position: Int)
    }
}