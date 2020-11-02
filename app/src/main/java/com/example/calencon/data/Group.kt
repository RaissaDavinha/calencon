package com.example.calencon.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(val id: String = "", var name: String = "", var url: String? = null, var usersList: MutableList<User>? = null) : Parcelable