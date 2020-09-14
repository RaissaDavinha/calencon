package com.example.calencon.data

data class Event(
    val user_id: String = "",
    val calendar_id: Long = 0,
    var title: String = "",
    var dtstart: Long = 0,
    var dtend: Long? = 0,
    var duration: String? = "",
    var all_day: Boolean? = false,
    var rrule: String? = "",
    var rdate: String? = "",
    var availability: Int? = 0
)