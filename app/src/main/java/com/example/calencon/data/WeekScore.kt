package com.example.calencon.data

enum class WeekScore(val weekDay: String, val dayScore: Double) {
    MONDAY("seg", 0.1),
    TUESDAY("ter", 0.25),
    WEDNESDAY("qua", 0.4),
    THURSDAY("qui", 0.55),
    SUNDAY("dom", 0.7),
    SATURDAY("sab", 0.85),
    FRIDAY("sex", 1.00)
}