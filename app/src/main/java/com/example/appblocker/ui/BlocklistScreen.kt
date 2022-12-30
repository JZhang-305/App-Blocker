package com.example.appblocker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.appblocker.backend.getBlocklistNames
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun loadDaysForBlocklist(context: Context): List<DayOfTheWeek>? {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    if (sharedPreferences.getString("days", null) == null) {
        return null
    }
    return Json.decodeFromString<List<DayOfTheWeek>>(sharedPreferences.getString("days", null)!!)
}

fun saveDaysForBlocklist(context: Context, days: List<DayOfTheWeek>) {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val json = Json.encodeToString(days)
    editor.putString("days", json)
    editor.apply()
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BlocklistScreen(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val database = context.getSharedPreferences("app_blocklists", Context.MODE_PRIVATE)
    val blocklistNames = remember { mutableStateListOf<String>(*getBlocklistNames(context)) }
    val contextForToast = LocalContext.current.applicationContext
    // Scaffold and FAB need to be coupled to create a persistent button
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 16.dp),
                //.align(alignment = Alignment.BottomEnd),
                onClick = {
                    navController.navigate("BlocklistSelectorScreen")
                }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }

    ) {
        Column {
            Spacer(modifier = modifier.height(15.dp))
            Text(
                "App Blocklists",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = modifier.height(15.dp))
            Divider(color = Color.Gray)
            //var counter: Int = 0
            LazyColumn {
                items(blocklistNames) { blocklist ->
                    BlocklistRow(
                        blocklist = blocklist,
                        database = database,
                        onBlocklistRemoved = {
                            blocklistNames.remove(blocklist)
                            val daysOfWeek = loadDaysForBlocklist(context)
                            if (daysOfWeek != null) {
                                for (day in daysOfWeek) {
                                    if (day.blocklistName == blocklist) {
                                        day.blocklistName = ""
                                    }
                                }
                            }
                            if (daysOfWeek != null) {
                                saveDaysForBlocklist(context, daysOfWeek)
                            }
                        }
                    )


                }
            }
        }
    }
}

@Composable
fun BlocklistRow(
    blocklist: String,
    database: SharedPreferences,
    onBlocklistRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        SimpleAlertDialog(
            blocklist = blocklist,
            database = database,
            showDialog = showDialog,
            onBlocklistRemoved = onBlocklistRemoved
        )
    }

    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                blocklist,
                fontSize = 30.sp,
                //fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(top = 7.dp, start = 7.dp)
                    .clickable { expanded = true })
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (appInBlocklist in database.getStringSet(blocklist, mutableSetOf())?.toList()
                    ?.sorted()!!) {
                    DropdownMenuItem(onClick = {}) {
                        Text(appInBlocklist)
                    }
                    Divider()
                }
            }
        }

        Icon(
            painter = painterResource(R.drawable.grey_trash_can),
            contentDescription = "Trash can button icon",
            modifier = modifier
                .padding(all = 7.dp)
                .align(Alignment.End)
                .clickable {
                    showDialog.value = true
                }
        )


    }
    Divider(color = Color.Gray)
}

@Composable
fun SimpleAlertDialog(
    blocklist: String,
    database: SharedPreferences,
    showDialog: MutableState<Boolean>,
    onBlocklistRemoved: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        dismissButton = {
            TextButton(onClick = { showDialog.value = false })
            { Text(text = "Cancel") }
        },
        confirmButton = {
            TextButton(onClick = {

                with(database.edit()) {
                    remove(blocklist)
                    apply()
                }
                onBlocklistRemoved()
                showDialog.value = false
            }
            )
            { Text(text = "OK") }
        },
        title = { Text(text = "Please confirm") },
        text = { Text(text = "Should I delete the '" + blocklist + "' blocklist?") }
    )
}