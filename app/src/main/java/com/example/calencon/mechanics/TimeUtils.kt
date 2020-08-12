package com.example.calencon.mechanics

import java.util.concurrent.TimeUnit


/**
 * Returns the 'current time' of the Worker in the specified time unit.
 * @param unit the time unit
 * @return the 'current time'
 */
fun now(unit: TimeUnit): Long {
    return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
}