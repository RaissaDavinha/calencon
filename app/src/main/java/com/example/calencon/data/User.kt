package com.example.calencon.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String = "", val name: String = "", val url: String = "", var isSelected: Boolean = false) : Parcelable