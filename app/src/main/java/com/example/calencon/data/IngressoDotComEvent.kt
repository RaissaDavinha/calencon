package com.example.calencon.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IngressoDotComEvent (val id: String = "", var title: String = "", val images: List<Images>? = null)

data class Images(val url: String, val type: String)

data class IngressoDotComEventResponse(@SerializedName("items")
                                 var eventsList: List<IngressoDotComEvent>? = null) : Serializable
