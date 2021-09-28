package com.example.calencon.data.remote

import com.example.calencon.data.IngressoDotComEventResponse
import retrofit2.Call
import retrofit2.http.GET

interface WebService {

    @GET("/events/partnership/calencon")
    fun getAllEvents(): Call<IngressoDotComEventResponse>
}