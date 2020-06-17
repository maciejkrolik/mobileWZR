package pl.expert.mobilewzr.ui.common.thread

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Message
import pl.expert.mobilewzr.util.toFormattedString

class ThreadRecyclerAdapter : ListAdapter<Message, ThreadRecyclerAdapter.MessagesViewHolder>(MessagesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            itemView.messageDate.text = message.date?.toFormattedString(withNewLine = false)
            itemView.messageContent.text = message.content
            if (message.isLecturerMessage == true) {
                (itemView as? CardView)?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorAccentGradient
                    )
                )
            } else {
                (itemView as? CardView)?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorPrimaryDark
                    )
                )
            }
        }
    }

    class MessagesDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.documentId == newItem.documentId
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

}