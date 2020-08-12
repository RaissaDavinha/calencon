package com.example.calencon.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calencon.R
import com.example.calencon.data.Message
import com.example.calencon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_from_message.view.*
import kotlinx.android.synthetic.main.item_to_message.view.*

class ChatAdapter(private val userId: String) : BaseRecyclerAdapter<Message, ChatAdapter.ViewHolder>() {
    private var isGroup: Boolean = false

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ChatViewType.FROM.id -> {
                FromViewHolder(
                    LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_from_message, viewGroup,
                        false
                    )
                )
            }
            else -> {
                ToViewHolder(
                    LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_to_message, viewGroup,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mData[position].fromId == userId) {
            ChatViewType.FROM.id
        } else {
            ChatViewType.TO.id
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mData[position])
    }

    override fun validateDate(): Boolean = false

    inner class FromViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bind(item: Message) {
            itemView.apply {
                txt_msg_from.text = item.text
            }
        }
    }

    inner class ToViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bind(item: Message) {
            itemView.apply {
                if (isGroup) {
                    user_name.text = item.name
                }
                txt_msg.text = item.text
            }
        }
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Message) {}
    }

    fun setIsGroup(b: Boolean) {
        isGroup = b
    }

    enum class ChatViewType(val id: Int) {
        FROM(1),
        TO(2)
    }
}