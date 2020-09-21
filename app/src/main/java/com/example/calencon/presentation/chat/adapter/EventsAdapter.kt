package com.example.calencon.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calencon.R
import com.example.calencon.data.Event
import com.example.calencon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.calendar_event_details.view.*
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter : BaseRecyclerAdapter<Event, EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.calendar_event_details, viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(k: ViewHolder, position: Int) {
        k.bind(mData[position])
    }

    override fun validateDate() = false

    open class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Event) {
            val hourFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

            view.apply {
                start_time.text = hourFormatter.format(item.dtstart)

                end_time.text = hourFormatter.format(item.dtend)

                title.text = item.title
            }
        }
    }
}