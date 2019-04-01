package pl.expert.mobilewzr.ui.timetableviews.weekview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.week_view_item.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.dto.WeekViewItem

class WeekViewRecyclerAdapter(
    private val dataset: List<WeekViewItem>,
    private val onSubjectListener: OnSubjectListener
) : RecyclerView.Adapter<WeekViewRecyclerAdapter.SubjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewRecyclerAdapter.SubjectsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.week_view_item, parent, false)
        return SubjectsViewHolder(itemView, onSubjectListener)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.timeTextView?.text = dataset[position].time
        holder.mondayTextView?.text = dataset[position].weekViewSubjectItems[0].title
        holder.tuesdayTextView?.text = dataset[position].weekViewSubjectItems[1].title
        holder.wednesdayTextView?.text = dataset[position].weekViewSubjectItems[2].title
        holder.thursdayTextView?.text = dataset[position].weekViewSubjectItems[3].title
        holder.fridayTextView?.text = dataset[position].weekViewSubjectItems[4].title
    }

    override fun getItemCount() = dataset.size

    class SubjectsViewHolder(itemView: View, onSubjectListener: OnSubjectListener) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        var timeTextView: TextView? = null
        var mondayTextView: TextView? = null
        var tuesdayTextView: TextView? = null
        var wednesdayTextView: TextView? = null
        var thursdayTextView: TextView? = null
        var fridayTextView: TextView? = null
        var onSubjectListener: OnSubjectListener? = null

        init {
            this.onSubjectListener = onSubjectListener

            timeTextView = itemView.class_time_text
            mondayTextView = itemView.monday_subject_text
            tuesdayTextView = itemView.tuesday_subject_text
            wednesdayTextView = itemView.wednesday_subject_text
            thursdayTextView = itemView.thursday_subject_text
            fridayTextView = itemView.friday_subject_text

            mondayTextView!!.setOnClickListener(this)
            tuesdayTextView!!.setOnClickListener(this)
            wednesdayTextView!!.setOnClickListener(this)
            thursdayTextView!!.setOnClickListener(this)
            fridayTextView!!.setOnClickListener(this)

            mondayTextView!!.setOnLongClickListener(this)
            tuesdayTextView!!.setOnLongClickListener(this)
            wednesdayTextView!!.setOnLongClickListener(this)
            thursdayTextView!!.setOnLongClickListener(this)
            fridayTextView!!.setOnLongClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view) {
                mondayTextView -> onSubjectListener?.onSubjectClick(adapterPosition, 0)
                tuesdayTextView -> onSubjectListener?.onSubjectClick(adapterPosition, 1)
                wednesdayTextView -> onSubjectListener?.onSubjectClick(adapterPosition, 2)
                thursdayTextView -> onSubjectListener?.onSubjectClick(adapterPosition, 3)
                fridayTextView -> onSubjectListener?.onSubjectClick(adapterPosition, 4)
            }
        }

        override fun onLongClick(view: View?): Boolean {
            when (view) {
                mondayTextView -> onSubjectListener?.onSubjectLongClick()
                tuesdayTextView -> onSubjectListener?.onSubjectLongClick()
                wednesdayTextView -> onSubjectListener?.onSubjectLongClick()
                thursdayTextView -> onSubjectListener?.onSubjectLongClick()
                fridayTextView -> onSubjectListener?.onSubjectLongClick()
            }
            return true
        }
    }

    interface OnSubjectListener {
        fun onSubjectClick(position: Int, dayOfWeek: Int)
        fun onSubjectLongClick()
    }
}