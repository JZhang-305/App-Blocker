package com.example.appblocker.ui

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appblocker.backend.DayOfTheWeek
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.*

@Composable
fun HomeScreen(navController: NavHostController, context: Context, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "App Blocker",
            fontSize = 55.sp,
            fontWeight = FontWeight.Bold,
            //modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(100.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { navController.navigate("ScheduleScreen") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "Schedule",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { navController.navigate("PlaylistScreen") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "App Playlists",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "Unlock",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Current State: ", color = Color.Black, fontSize = 30.sp)

            if (isBlocking(context) == true) { Text(text = "Active", color = Color.Green) }
            else { Text(text = "Inactive", color = Color.Red) }
        }

        //Button(onClick = {navController.navigate("PlaylistScreen")}) { Text("App Playlist") }
        //Button(onClick = {}) { Text("Unlock") }
    }
}

fun loadDays(context: Context): List<DayOfTheWeek>? {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    if (sharedPreferences.getString("days", null) == null) {
        return null
    }
    return Json.decodeFromString<List<DayOfTheWeek>>(sharedPreferences.getString("days", null)!!)
}

fun isBlocking(context: Context): Boolean? {
    val calendar = Calendar.getInstance()
    val dayNum = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val days: List<DayOfTheWeek>? = loadDays(context)
    if (loadDays(context) != null) {
        days?.get(dayNum)?.updateIsCurrentlyBlocking()
        return days?.get(dayNum)?.currentlyBlocking
    }
    else {return false}
}

/*
fun currentDay(): String {
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
}*/