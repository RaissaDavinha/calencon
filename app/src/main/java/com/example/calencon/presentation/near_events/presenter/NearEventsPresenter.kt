package com.example.calencon.presentation.near_events.presenter

import com.example.calencon.data.IngressoDotComEventResponse
import com.example.calencon.data.remote.WebServiceClient
import com.example.calencon.presentation.near_events.NearEventsInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearEventsPresenter(private val webService: WebServiceClient, val nearEventsInterface: NearEventsInterface) {

    fun getEvents() {
        val call = webService.getAllEvents().getAllEvents()

        call.enqueue(object : Callback<IngressoDotComEventResponse?> {
            override fun onResponse(
                call: Call<IngressoDotComEventResponse?>?,
                response: Response<IngressoDotComEventResponse?>?
            ) {
                response?.body()?.eventsList?.let {
                    nearEventsInterface.fetchEvents(it)
                }
            }

            override fun onFailure(call: Call<IngressoDotComEventResponse?>?, t: Throwable?) {
                // tratar algum erro
            }
        })
    }
}