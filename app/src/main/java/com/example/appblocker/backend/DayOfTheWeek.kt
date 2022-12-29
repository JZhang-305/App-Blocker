package com.example.appblocker.backend

import java.time.LocalDate
import java.util.*
import java.time.DayOfWeek


class DayOfTheWeek(val dayOfWeek: String) {
    var sleepTime = 0
    var wakeTime = 0
    var playlistName = ""
    var appsInPlaylist = listOf<String>()
    var isToday = (dayOfWeek == currentDay())


    private fun currentDay(): String {
        val calendar = Calendar.getInstance();
        val day = calendar.get(Calendar.DAY_OF_WEEK);
        return when (day) {
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