package com.example.calencon.business.IngressoDotCom

import com.example.calencon.data.IngressoDotComEvent
import com.example.calencon.data.IngressoDotComEventResponse
import com.example.calencon.data.User
import com.example.calencon.data.remote.WebServiceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngressoDotComBO (val webService: WebServiceClient){

    fun getEvents(): List<IngressoDotComEvent>? {
        val call = webService.getAllEvents().getAllEvents()
        var callResponse: List<IngressoDotComEvent>? = null

        call.enqueue(object : Callback<IngressoDotComEventResponse?> {
            override fun onResponse(call: Call<IngressoDotComEventResponse?>?, response: Response<IngressoDotComEventResponse?>?) {
                callResponse = response?.body()?.eventsList
            }

            override fun onFailure(call: Call<IngressoDotComEventResponse?>?, t: Throwable?) {
                // tratar algum erro
            }
        })

        return callResponse
    }
}