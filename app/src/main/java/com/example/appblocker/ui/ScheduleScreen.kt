package com.example.appblocker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appblocker.backend.DayOfTheWeek
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appblocker.backend.getPlaylistNames
import android.app.TimePickerDialog

fun loadDaysFromPreferences(context: Context): List<DayOfTheWeek>? {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    if (sharedPreferences.getString("days", null) == null) {
        return null
    }
    return Json.decodeFromString<List<DayOfTheWeek>>(sharedPreferences.getString("days", null)!!)
}

fun saveDaysToPreferences(context: Context, days: List<DayOfTheWeek>) {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val json = Json.encodeToString(days)
    editor.putString("days", json)
    editor.apply()
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleScreen(context: Context, modifier: Modifier = Modifier, navController: NavHostController? = null) {
    // if preferences have not been set yet (first time setup), force default options
    val listOfDayObjects = loadDaysFromPreferences(context)
    if (listOfDayObjects == null) {
        val dayNames =
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val weekData = remember { mutableStateListOf<DayOfTheWeek>() }
        for (day in dayNames) {
            weekData.add(DayOfTheWeek(day))
        }
        saveDaysToPreferences(context, weekData)
    }


    val playlistNames = getPlaylistNames(context).toList()
    val switchStates = remember {
        mutableStateMapOf<String, Boolean>(
            "Sunday" to false,
            "Monday" to false,
            "Tuesday" to false,
            "Wednesday" to false,
            "Thursday" to false,
            "Friday" to false,
            "Saturday" to false
        )
    }
    val weekData =
        remember { loadDaysFromPreferences(context)?.let { mutableStateListOf<DayOfTheWeek>(*it.toTypedArray()) } }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Save") },
                onClick = {
                    if (weekData != null) {
                        saveDaysToPreferences(context, weekData)
                        Toast.makeText(context, "Preferences Saved", Toast.LENGTH_SHORT).show()
                    }
                },
                icon = {Icon(imageVector = Icons.Filled.Done, contentDescription = "Done")},
                modifier = Modifier.padding(all = 16.dp))

            }

    ) {
        Column {
            Spacer(modifier = modifier.height(15.dp))
            Text(
                "Schedule",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = modifier.height(15.dp))
            Divider(color = Color.Gray)
            Spacer(modifier = modifier.height(10.dp))
            LazyColumn {
                // for state hoisting switch

                if (weekData != null) {

                    items(weekData.toList()) { day ->
                        // make each row
                        //ScheduleRow(day = day, playlistNames = playlistNames)

                        // make every row curved
                        Row(
                            verticalAlignment = Alignment.CenterVertically, modifier = modifier
                                .padding(5.dp)
                                .height(120.dp)
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(2.dp, Color.Red), shape = RoundedCornerShape(20.dp)
                                )
                        ) {
                            Column(
                                modifier = modifier.widthIn(0.dp, 80.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(day.dayOfWeek.take(3), fontSize = 30.sp, fontWeight = FontWeight.SemiBold
                                    //, modifier = modifier.padding(5.dp)
                                )

                                switchStates[day.dayOfWeek]?.let {
                                    Switch(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(Alignment.CenterHorizontally),
                                        checked = day.isSwitchedOn,
                                        onCheckedChange = {
                                            switchStates[day.dayOfWeek] =
                                                !switchStates[day.dayOfWeek]!!
                                            day.isSwitchedOn = !day.isSwitchedOn
                                        },
                                        colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
                                    )
                                }
                            }
                            Spacer(modifier = modifier.width(10.dp))
                            //Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))


                            // CODE FOR APP PLAYLIST DROPDOWN
                            val contextForToast = LocalContext.current.applicationContext
                            var firstPlaylistName = ""
                            firstPlaylistName =
                                if (playlistNames.isNotEmpty() && day.playlistName == "") {
                                    playlistNames[0]
                                } else {
                                    day.playlistName
                                }

                            var selectedItem by remember { mutableStateOf(firstPlaylistName) }
                            var expanded by remember {
                                mutableStateOf(false)
                            }
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = {
                                    expanded = !expanded
                                }
                            ) {

                                // text field
                                TextField(
                                    value = selectedItem,
                                    onValueChange = {
                                        selectedItem = it
                                        day.playlistName = selectedItem
                                    },
                                    readOnly = true,
                                    label = { Text(text = "App Playlist") },
                                    modifier = Modifier.widthIn(0.dp, 180.dp),
                                    singleLine = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                                )

                                // menu
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    playlistNames.forEach { selectedOption ->
                                        // menu item
                                        DropdownMenuItem(onClick = {
                                            selectedItem = selectedOption
                                            day.playlistName = selectedItem
                                            //Toast.makeText(contextForToast, selectedOption, Toast.LENGTH_SHORT).show()
                                            expanded = false
                                        }) {
                                            Text(text = selectedOption)
                                        }
                                    }
                                }
                            }

                        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            // code for top row with moon icon and bed time
                            Row() {
                                Icon(imageVector = Icons.Filled.SingleBed, contentDescription = "Moon")

                            }
                            // code for top row with sun icon and wake time
                            Row() {
                                Icon(imageVector = Icons.Filled.WbSunny, contentDescription = "Sun")
                            }
                        }

                        }
                    }
                }
            }
        }
    }
}


/*
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleRow(day: DayOfTheWeek, playlistNames: List<String>, modifier: Modifier = Modifier) {
    // make every row curved
    Row (verticalAlignment = Alignment.CenterVertically, modifier = modifier
        .padding(15.dp)
        .height(160.dp)
        .fillMaxWidth()
        .border(
            BorderStroke(2.dp, Color.Red), shape = RoundedCornerShape(20.dp)
        )
    ) {
        Column {
            Text(day.dayOfWeek.take(3), fontSize = 30.sp, fontWeight = FontWeight.SemiBold, modifier = modifier.padding(10.dp))
            Switch (
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.Start),
                checked = day.isSwitchedOn,
                onCheckedChange = {
                    day.isSwitchedOn = !day.isSwitchedOn
                                  } ,
                colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
            )
        }
        Spacer(modifier = modifier.width(15.dp))
        //Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))


        // CODE FOR APP PLAYLIST DROPDOWN
        val contextForToast = LocalContext.current.applicationContext
        var firstPlaylistName = ""
        if (playlistNames.isNotEmpty()) {
            firstPlaylistName = playlistNames[0]
        }

        var selectedItem by remember {mutableStateOf(firstPlaylistName)}
        var expanded by remember {
            mutableStateOf(false)
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            // text field
            TextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "App Playlist") },
                modifier = Modifier.widthIn(0.dp, 180.dp),
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            // menu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                playlistNames.forEach { selectedOption ->
                    // menu item
                    DropdownMenuItem(onClick = {
                        selectedItem = selectedOption
                        //Toast.makeText(contextForToast, selectedOption, Toast.LENGTH_SHORT).show()
                        expanded = false
                    }) {
                        Text(text = selectedOption)
                    }
                }
            }
        }
    }
}*/