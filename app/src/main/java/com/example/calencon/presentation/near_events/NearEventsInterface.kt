package com.example.calencon.presentation.near_events

import com.example.calencon.data.IngressoDotComEvent

interface NearEventsInterface {
    fun fetchEvents(eventList: List<IngressoDotComEvent>)
}