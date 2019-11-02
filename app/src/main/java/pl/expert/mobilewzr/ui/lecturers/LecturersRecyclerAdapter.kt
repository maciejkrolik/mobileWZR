package pl.expert.mobilewzr.ui.lecturers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_lecturer.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Lecturer
import java.util.*

class LecturersRecyclerAdapter(
    private val lecturers: MutableList<Lecturer>,
    private val onLecturerListener: OnLecturerListener
) : RecyclerView.Adapter<LecturersRecyclerAdapter.LecturersViewHolder>() {

    private val fullList = mutableListOf<Lecturer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lecturer, parent, false)
        return LecturersViewHolder(itemView, onLecturerListener)
    }

    override fun onBindViewHolder(holder: LecturersViewHolder, position: Int) {
        holder.lecturerNameTextView?.text = lecturers[position].name
        holder.lecturerEmailTextView?.text = lecturers[position].email
        holder.lecturerPhoneTextView?.text = lecturers[position].phone
        holder.lecturerRoomTextView?.text = lecturers[position].room
        holder.lecturerInfoTextView?.text = lecturers[position].info
    }

    override fun getItemCount() = lecturers.size

    fun setItems(lecturers: List<Lecturer>) {
        this.lecturers.clear()
        this.lecturers.addAll(lecturers)
        notifyDataSetChanged()
    }

    fun refreshFullList(lecturers: List<Lecturer>) {
        this.fullList.clear()
        this.fullList.addAll(lecturers)
    }

    fun filterList(input: String) {
        lecturers.clear()
        for (lecturer in fullList)
            if (lecturer.name.toLowerCase(Locale.UK).contains(input.toLowerCase(Locale.UK)))
                lecturers.add(lecturer)
        notifyDataSetChanged()
    }

    class LecturersViewHolder(itemView: View, onLecturerListener: OnLecturerListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var lecturerNameTextView: TextView? = null
        var lecturerEmailTextView: TextView? = null
        var lecturerPhoneTextView: TextView? = null
        var lecturerRoomTextView: TextView? = null
        var lecturerInfoTextView: TextView? = null
        var onLecturerListener: OnLecturerListener? = null

        init {
            lecturerNameTextView = itemView.lecturerName
            lecturerEmailTextView = itemView.lecturerEmail
            lecturerPhoneTextView = itemView.lecturerPhone
            lecturerRoomTextView = itemView.lecturerRoom
            lecturerInfoTextView = itemView.lecturerInfo

            this.onLecturerListener = onLecturerListener
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onLecturerListener?.onLecturerClick(adapterPosition)
        }
    }

    interface OnLecturerListener {
        fun onLecturerClick(position: Int)
    }

}