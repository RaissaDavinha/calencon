package com.example.calencon.presentation.chat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calencon.data.Event
import com.example.calencon.data.Message
import com.example.calencon.presentation.base.BaseRecyclerAdapter

class EventsAdapter: BaseRecyclerAdapter<Event, EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EventsAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(k: EventsAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun validateDate(): Boolean {
        TODO("Not yet implemented")
    }
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Message) {}
    }
}