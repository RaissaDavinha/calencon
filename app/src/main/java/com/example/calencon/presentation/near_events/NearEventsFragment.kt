package com.example.calencon.presentation.near_events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calencon.R
import com.example.calencon.data.IngressoDotComEvent
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.contacts_fragment.*

class NearEventsFragment : Fragment() {
    private lateinit var eventsAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.near_events_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        fetchEvents()
    }

    private fun fetchEvents() {
        eventsAdapter.add(NearEventsViewHolder(IngressoDotComEvent(id = "", title = "teste 1")))
        eventsAdapter.add(NearEventsViewHolder(IngressoDotComEvent(id = "", title = "teste 2")))
        eventsAdapter.add(NearEventsViewHolder(IngressoDotComEvent(id = "", title = "teste 3")))
        eventsAdapter.add(NearEventsViewHolder(IngressoDotComEvent(id = "", title = "teste 4")))
        eventsAdapter.add(NearEventsViewHolder(IngressoDotComEvent(id = "", title = "teste 5")))
        eventsAdapter.add(NearEventsViewHolder(IngressoDotComEvent(id = "", title = "teste 6")))
    }

    private fun setAdapter() {
        eventsAdapter = GroupAdapter()
        eventsAdapter.setOnItemClickListener { item, _ ->

        }
        contacts_recycler_view.adapter = eventsAdapter
        contacts_recycler_view.layoutManager = GridLayoutManager(context, 2)
    }

    companion object {
        fun newInstance(): NearEventsFragment {
            return NearEventsFragment()
        }
    }
}