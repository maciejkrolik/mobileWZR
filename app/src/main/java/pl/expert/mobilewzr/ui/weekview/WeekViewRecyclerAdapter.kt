package pl.expert.mobilewzr.ui.weekview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.WeekViewItem

class WeekViewRecyclerAdapter(private val dataset: List<WeekViewItem>) :
    RecyclerView.Adapter<WeekViewRecyclerAdapter.SubjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewRecyclerAdapter.SubjectsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.week_view_item, parent, false)
        return SubjectsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.timeTextView?.text = dataset[position].time
        holder.mondayTextView?.text = dataset[position].listOfSubjects[0]
        holder.tuesdayTextView?.text = dataset[position].listOfSubjects[1]
        holder.wednesdayTextView?.text = dataset[position].listOfSubjects[2]
        holder.thursdayTextView?.text = dataset[position].listOfSubjects[3]
        holder.fridayTextView?.text = dataset[position].listOfSubjects[4]
    }

    override fun getItemCount() = dataset.size

    class SubjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeTextView: TextView? = null
        var mondayTextView: TextView? = null
        var tuesdayTextView: TextView? = null
        var wednesdayTextView: TextView? = null
        var thursdayTextView: TextView? = null
        var fridayTextView: TextView? = null

        init {
            timeTextView = itemView.findViewById(R.id.class_time_text)
            mondayTextView = itemView.findViewById(R.id.monday_subject_text)
            tuesdayTextView = itemView.findViewById(R.id.tuesday_subject_text)
            wednesdayTextView = itemView.findViewById(R.id.wednesday_subject_text)
            thursdayTextView = itemView.findViewById(R.id.thursday_subject_text)
            fridayTextView = itemView.findViewById(R.id.friday_subject_text)
        }
    }
}