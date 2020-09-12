package com.example.calencon.business.geneticAlgorithm

import com.example.calencon.data.Event
import java.util.*
import java.util.concurrent.TimeUnit

//Specimen = one solution
//Specimens are represented by the Event to be created
class Specimen(userId: String = "", calendarId: Long, eventTitle: String = "", dtStart: Long = 0, dtEnd: Long? = 0,
               eventDuration: String? = "", allDay: Boolean? = false, rRule: String? = "", rDate: String? = "", userAvailability: Int? = 0){

    private val individual = Event(userId, calendarId, eventTitle, dtStart, dtEnd, eventDuration, allDay, rRule, rDate, userAvailability)
    private var fitnessScore = 0f

    //number of 30-minute blocks past 12am.
    private var startTime = startTimeToBlock()
    private var endTime = endTimeToBlock()

    private fun startTimeToBlock(): Int {
        val date = Calendar.getInstance()
        date.timeInMillis = individual.dtstart

        //hour of day = 24h type
        var startTimeBlock = date.get(Calendar.HOUR_OF_DAY) * 2
        if (date.get(Calendar.MINUTE) >= 30) startTimeBlock++

        return startTimeBlock
    }

    private fun endTimeToBlock(): Int {
        val date = Calendar.getInstance()

        if (individual.dtend != null) {
            date.timeInMillis = individual.dtend!!
        } else {
            val aHour = TimeUnit.HOURS.toMillis(1)
            date.timeInMillis = individual.dtstart + aHour
        }

        //hour of day = 24h type
        var endTimeBlock = date.get(Calendar.HOUR_OF_DAY) * 2
        if (date.get(Calendar.MINUTE) >= 30) endTimeBlock++

        return endTimeBlock
    }

    fun getSpecimen() = individual

    fun getFitnessScore() = fitnessScore

    fun getDay(): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    fun getMonth(): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        return cal.get(Calendar.MONTH)
    }

    fun getHour(): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    fun getstartTime() = startTime

    fun getendTime() = endTime

    fun getYear(): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        return cal.get(Calendar.YEAR)
    }

    fun getMinutes(): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        return cal.get(Calendar.MINUTE)
    }

    fun getDtStart() = individual.dtstart

    fun getDtEnd() = individual.dtend ?: 0

    fun getEventDuration() = individual.duration ?: ""

    fun getAllDay() = individual.all_day ?: false

    fun setDtStart(timeInMillis: Long) {
        individual.dtstart = timeInMillis
        startTime = startTimeToBlock()
    }

    fun setDtStart(year: Int, month: Int, day: Int, hour: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month, day, hour, 0)
        individual.dtstart = cal.timeInMillis

        startTime = startTimeToBlock()
    }

    fun setDay(day: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        cal.set(Calendar.DAY_OF_MONTH, day)
        individual.dtstart = cal.timeInMillis
    }

    fun setMonth(month: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        cal.set(Calendar.MONTH, month)
        individual.dtstart = cal.timeInMillis
    }

    fun setYear(year: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        cal.set(Calendar.YEAR, year)
        individual.dtstart = cal.timeInMillis
    }

    fun setStartHour(hour: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        cal.set(Calendar.HOUR_OF_DAY, hour)
        individual.dtstart = cal.timeInMillis

        startTime = startTimeToBlock()
    }

    fun setStartHour(hour: Int, minute:Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = individual.dtstart
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        individual.dtstart = cal.timeInMillis

        startTime = startTimeToBlock()
    }

    fun setDuration(durationInMillis: Long) {
        individual.dtend = individual.dtstart + durationInMillis
        endTime = endTimeToBlock()
    }

    fun setFitnessScore() {
        fitnessScore = FitnessCalc().fitnessScore(this)
    }
}