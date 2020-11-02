package com.example.calencon.business.connection

import com.example.calencon.data.IngressoDotComEventResponse
import retrofit2.Call
import retrofit2.http.GET

interface WebService {

    @GET("/events/partnership/calencon")
    fun getAllEvents(): Call<IngressoDotComEventResponse>
}