package com.example.calencon.data.remote

import com.example.calencon.data.URL
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebServiceClient {
    lateinit var retrofit: Retrofit

    fun createService() {
        //Instancia do interceptador das requisições
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient= OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS)
        httpClient.addInterceptor(loggingInterceptor)

        //Instância do retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(httpClient.build())
            .build()
    }

    fun getAllEvents(): WebService {
        return this.retrofit.create(WebService::class.java)
    }
}