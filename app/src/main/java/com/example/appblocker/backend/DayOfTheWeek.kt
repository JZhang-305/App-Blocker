package com.example.appblocker.backend

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.*
import java.time.DayOfWeek
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Serializable
class DayOfTheWeek(val dayOfWeek: String) {
    //var sleepTimeHour = 0
    var sleepTime = "00:00"
    var wakeTime = "00:01"
    var blocklistName = ""
    var appsInBlocklist = listOf<String>()
    var isToday = (dayOfWeek == currentDay())
    var isSwitchedOn = false
    var currentlyBlocking = false

    fun updateIsCurrentlyBlocking() {
        updateIsToday()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)

        currentlyBlocking = (isSwitchedOn && isToday && isInBetween(
            sleepTime,
            wakeTime,
            formatted
        ) && sleepTime != wakeTime)
    }

    private fun currentDay(): String {
        val calendar = Calendar.getInstance();
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"

            else -> {
                "Error"
            }
        }
    }

    fun updateIsToday() {
        isToday = (dayOfWeek == currentDay())
    }

    private fun isInBetween(
        startTime: String,
        endTime: String,
        currentTime: String
    ): Boolean {
        val startMinutesSinceMidnight = calculateMinutesSinceMidnight(startTime)
        val endMinutesSinceMidnight = calculateMinutesSinceMidnight(endTime)
        val currentMinutesSinceMidnight = calculateMinutesSinceMidnight(currentTime)
        if (startMinutesSinceMidnight < endMinutesSinceMidnight) {
            return (currentMinutesSinceMidnight >= startMinutesSinceMidnight) && (currentMinutesSinceMidnight < endMinutesSinceMidnight)
        } else {
            return !((currentMinutesSinceMidnight >= endMinutesSinceMidnight) && (currentMinutesSinceMidnight < startMinutesSinceMidnight))
        }
    }

    private fun calculateMinutesSinceMidnight(time_hh_mm: String): Int {
        val timeStrArray = time_hh_mm.split(":")
        var minutes = timeStrArray[1].toInt()
        minutes += 60 * timeStrArray[0].toInt()
        return minutes
    }
}