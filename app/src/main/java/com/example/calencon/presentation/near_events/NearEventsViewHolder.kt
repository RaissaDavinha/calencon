package com.example.calencon.presentation.near_events

import com.example.calencon.R
import com.example.calencon.data.IngressoDotComEvent
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.near_events_item.view.*

class NearEventsViewHolder (private val mEvent: IngressoDotComEvent) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.near_events_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.event_title.text = mEvent.title
        //todo add image
    }
}