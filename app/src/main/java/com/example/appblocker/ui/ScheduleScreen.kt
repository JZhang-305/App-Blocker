package com.example.appblocker.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appblocker.backend.getPlaylistNames

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleScreen(context: Context, modifier: Modifier = Modifier, navController: NavHostController? = null) {
    // if preferences have not been set yet (first time setup), force default options
    val listOfDayObjects = loadDaysFromPreferences(context)
    if (listOfDayObjects == null) {
        val dayNames = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val weekData = remember { mutableStateListOf<DayOfTheWeek>() }
        for (day in dayNames) {
            weekData.add(DayOfTheWeek(day))
        }
        saveDaysToPreferences(context, weekData)
    }
    val playlistNames = getPlaylistNames(context).toList()
    val weekData = remember { loadDaysFromPreferences(context)?.let { mutableStateListOf<DayOfTheWeek>(*it.toTypedArray()) } }

    Column {
        Spacer(modifier = modifier.height(15.dp))
        Text(
            "Schedule",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = modifier.height(15.dp))
        Divider(color = Color.Gray)
        Spacer(modifier = modifier.height(10.dp))
    LazyColumn () {
        if (weekData != null) {
            items(weekData.toList()) {day ->
                // make each row curved
                Row (verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(15.dp).height(80.dp).fillMaxWidth().border(
                    BorderStroke(2.dp, Color.Red), shape = RoundedCornerShape(20.dp))
                ) {
                    Text(day.dayOfWeek.take(3), fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = modifier.padding(10.dp))
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
                            label = { Text(text = "Label") },
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
/*
                    TextField(
                        value = selectedItem,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Label") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )*/
                }

            }
        }
    }
}
}