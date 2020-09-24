package pl.expert.mobilewzr.ui.lecturers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_lecturer.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Lecturer
import java.util.*

class LecturersRecyclerAdapter(
    private val onLecturerListener: (Lecturer) -> Unit
) : ListAdapter<Lecturer, LecturersRecyclerAdapter.LecturersViewHolder>(LecturersDiffCallback()) {

    private val fullList = mutableListOf<Lecturer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lecturer, parent, false)
        return LecturersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LecturersViewHolder, position: Int) {
        holder.bind(getItem(position), onLecturerListener)
    }

    fun setItems(list: List<Lecturer>?) {
        fullList.clear()
        fullList.addAll(list ?: emptyList())
        submitList(list)
    }

    fun filterList(input: String) {
        val filteredList = mutableListOf<Lecturer>()
        for (lecturer in fullList)
            if (lecturer.name.toLowerCase(Locale.UK).contains(input.toLowerCase(Locale.UK)))
                filteredList.add(lecturer)
        submitList(filteredList)
    }

    class LecturersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(lecturer: Lecturer, onLecturerListener: (Lecturer) -> Unit) {
            itemView.apply {
                lecturerName.text = lecturer.name
                lecturerEmail.text = lecturer.email
                lecturerEmail.isVisible = lecturer.email.isNotBlank()
                lecturerPhone.text = lecturer.phone
                lecturerPhone.isVisible = lecturer.phone.isNotBlank()
                lecturerRoom.text = lecturer.room
                lecturerRoom.isVisible = lecturer.room.isNotBlank()
                lecturerInfo.text = lecturer.info
                lecturerInfo.isVisible = lecturer.info.isNotBlank()
                setOnClickListener { onLecturerListener(lecturer) }
            }
        }
    }

    class LecturersDiffCallback : DiffUtil.ItemCallback<Lecturer>() {
        override fun areItemsTheSame(oldItem: Lecturer, newItem: Lecturer): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Lecturer, newItem: Lecturer): Boolean {
            return oldItem == newItem
        }
    }

}