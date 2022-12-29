package com.example.appblocker.ui

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
import java.time.LocalDate
import java.util.*

@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
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
        //Button(onClick = {navController.navigate("PlaylistScreen")}) { Text("App Playlist") }
        //Button(onClick = {}) { Text("Unlock") }
    }
}