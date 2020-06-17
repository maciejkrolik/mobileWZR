package pl.expert.mobilewzr.ui.lecturers.lecturersmessages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_thread.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.domain.domainmodel.ThreadItem
import pl.expert.mobilewzr.util.toFormattedString

class LecturersMessagesRecyclerAdapter(
    private val onThreadItemListener: (ThreadItem) -> Unit
) :
    ListAdapter<ThreadItem, LecturersMessagesRecyclerAdapter.ThreadsViewHolder>(ThreadsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_thread, parent, false)
        return ThreadsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ThreadsViewHolder, position: Int) {
        holder.bind(getItem(position), onThreadItemListener)
    }

    class ThreadsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(thread: ThreadItem, onThreadItemListener: (ThreadItem) -> Unit) {
            itemView.threadGroup.text = thread.groupId
            itemView.threadLastMessage.text = thread.lastMessageContent
            itemView.threadLastMessageDate.text = thread.lastMessageDate?.toFormattedString()
            itemView.setOnClickListener { onThreadItemListener(thread) }
        }
    }

    class ThreadsDiffCallback : DiffUtil.ItemCallback<ThreadItem>() {
        override fun areItemsTheSame(oldItem: ThreadItem, newItem: ThreadItem): Boolean {
            return oldItem.lastMessageDocId == newItem.lastMessageDocId
        }

        override fun areContentsTheSame(oldItem: ThreadItem, newItem: ThreadItem): Boolean {
            return oldItem == newItem
        }
    }

}