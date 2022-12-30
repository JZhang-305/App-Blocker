package com.example.appblocker.ui
//package com.geeksforgeeks.jctimepicker

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appblocker.R
import com.example.appblocker.backend.DayOfTheWeek
import com.example.appblocker.backend.getPlaylistNames
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

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
fun ScheduleScreen(
    context: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController? = null
) {
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
    val todayColor: Color = Color.Black

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
                icon = { Icon(imageVector = Icons.Filled.Done, contentDescription = "Done") },
                modifier = Modifier.padding(all = 16.dp)
            )

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
                        var rowModifier = modifier
                            .padding(5.dp)
                            .height(120.dp)
                            .fillMaxWidth()

                        day.updateIsToday()
                        if (day.isToday) {
                            rowModifier = rowModifier.border(
                                BorderStroke(4.dp, todayColor),
                                shape = RoundedCornerShape(20.dp)
                            )
                        } else {
                            rowModifier = rowModifier.border(
                                BorderStroke(2.dp, Color.LightGray),
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically, modifier = rowModifier
                        ) {
                            // code to change color based on day
                            val colModifier = modifier.widthIn(0.dp, 80.dp)
//                            day.updateIsToday()
//                            if (day.isToday) {
//                                colModifier = colModifier.background(Color.Green)
//                            }

                            Column(
                                modifier = colModifier,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {


                                Text(
                                    day.dayOfWeek.take(3),
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.SemiBold
                                    //, modifier = modifier.padding(5.dp)
                                )
                                /*
                                Text(
                                    day.sleepTime,
                                    fontSize = 5.sp,
                                    fontWeight = FontWeight.SemiBold
                                    //, modifier = modifier.padding(5.dp)
                                )*/

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
                                        colors = SwitchDefaults.colors(
                                            uncheckedThumbColor = Color.DarkGray,
                                            checkedThumbColor = Color.Blue
                                        )
                                    )
                                }
                            }

                            /*
                            if (day.isToday) {
                                Divider(color = todayColor, modifier = Modifier
                                    .fillMaxHeight()
                                    .width(4.dp))

                            }
                            else {
                                Divider(color = Color.LightGray, modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2.dp))
                            }*/

                            Spacer(modifier = modifier.width(11.dp))

                            // CODE FOR APP PLAYLIST DROPDOWN
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
                                    modifier = Modifier.widthIn(0.dp, 140.dp),
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

                            Column(
                                modifier = modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // code for top row with moon icon and bed time
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.single_bed),
                                        contentDescription = "Bed"
                                    )
                                    //Text(":", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = modifier.width(4.dp))
                                    TimePickerFunction(day, sleepTime = true)
                                }
                                //code for top row with sun icon and wake time
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.wb_sunny),
                                        contentDescription = "Sun"
                                    )
                                    //Text(":", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = modifier.width(4.dp))
                                    TimePickerFunction(day, sleepTime = false)
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

// Creating a composable function
// to create a Time Picker
// Calling this function as content
// in the above function
@Composable
fun TimePickerFunction(day: DayOfTheWeek, sleepTime: Boolean, modifier: Modifier = Modifier) {

    // Fetching local context
    val mContext = LocalContext.current

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val mTime = remember { mutableStateOf("") }
    if (sleepTime) {mTime.value = day.sleepTime }
    else {mTime.value = day.wakeTime}


    // Creating a TimePicker dialog
        val mTimePickerDialog = TimePickerDialog(
            mContext,
            { _, mHour: Int, mMinute: Int ->
                mTime.value =
                    mHour.toString().padStart(2, '0') + ":" + mMinute.toString().padStart(2, '0')
                if (sleepTime) {
                    day.sleepTime = mTime.value
                }
                else {
                    day.wakeTime = mTime.value
                }
            }, day.sleepTime.take(2).toInt(), day.sleepTime.takeLast(2).toInt(), false
        )


    Button(
        onClick = {
            mTimePickerDialog.show()

        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White), modifier = modifier
            .padding(5.dp)
            .height(40.dp)
            .width(80.dp)
    ) {
        Text(text = mTime.value, color = Color.Black, fontSize = 15.sp)
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