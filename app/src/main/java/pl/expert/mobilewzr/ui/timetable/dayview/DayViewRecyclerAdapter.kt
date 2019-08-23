package pl.expert.mobilewzr.ui.timetable.dayview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_day_view.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.domain.domainmodel.DayViewItem

class DayViewRecyclerAdapter(
    private val dataSet: List<DayViewItem>,
    private val onSubjectListener: OnSubjectListener
) : RecyclerView.Adapter<DayViewRecyclerAdapter.SubjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_day_view, parent, false)
        return SubjectsViewHolder(itemView, onSubjectListener)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.startTimeTextView?.text = dataSet[position].startTime
        holder.endTimeTextView?.text = dataSet[position].endTime
        holder.titleTextView?.text = dataSet[position].title
        holder.locationWithDescriptionTextView?.text = dataSet[position].locationWithDescription
    }

    override fun getItemCount() = dataSet.size

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