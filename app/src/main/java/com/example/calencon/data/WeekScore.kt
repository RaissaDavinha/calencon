package com.example.calencon.data

enum class WeekScore(val weekDay: String, val dayScore: Double) {
    SUNDAY("dom", 0.55),
    MONDAY("seg", 0.0),
    TUESDAY("ter", 0.1),
    WEDNESDAY("qua", 0.25),
    THURSDAY("qui", 0.4),
    FRIDAY("sex", 0.85),
    SATURDAY("sab", 0.7)
}