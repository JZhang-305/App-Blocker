package com.example.appblocker.backend

import androidx.compose.runtime.*
import java.util.*
import java.time.DayOfWeek
import kotlinx.serialization.Serializable


@Serializable
class DayOfTheWeek(val dayOfWeek: String) {
    //var sleepTimeHour = 0
    var sleepTime = "10:00"
    var wakeTime = "00:00"
    var playlistName = ""
    var appsInPlaylist = listOf<String>()
    var isToday = (dayOfWeek == currentDay())
    var isSwitchedOn = false
    var currentlyBlocking = false



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
}