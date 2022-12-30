package com.example.appblocker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BlocklistSelectorScreen(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val packageManager = context.packageManager
    val database = context.getSharedPreferences("app_blocklists", Context.MODE_PRIVATE)
    // current set blocklist name in TextField
    var blocklistName: String by remember { mutableStateOf("") }
    // currently checked apps
    val checkedApps = remember { mutableStateListOf<String>() }

    val listOfApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    // For speeding up process of displaying all apps
    val appsByName = mutableMapOf<String, ApplicationInfo>()
    for (app in listOfApps) {
        if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
            appsByName[app.loadLabel(packageManager).toString()] = app
        }
    }
    val appNames = mutableListOf<String>()
    for ((appName, _) in appsByName) {
        appNames.add(appName)
    }
    appNames.sort()

    // Scaffold, bottomBar, and BottomAppBar need to be coupled to create a persistent bottom bar
    Scaffold(
        bottomBar = {
            BottomAppBar {
                TextField(
                    value = blocklistName,
                    onValueChange = {
                        blocklistName = it
                    },
                    label = { Text(text = "Name of Blocklist", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier.widthIn(0.dp, 350.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // When clicked, button should save a new blocklist and navigate back to screen displaying all blocklists
                Button(
                    onClick = {
                        if (blocklistName.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Must name blocklist before saving",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (checkedApps.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Must add app(s) to blocklist before saving",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // code to add key-value pair to database <blocklistName, apps in said blocklist>
                            with(database.edit()) {
                                putStringSet(blocklistName, checkedApps.toMutableSet())
                                apply()
                            }
                            navController.navigate("BlocklistScreen")

                        }
                    }
                ) {
                    Text("Save")
                }
            }

        }
    ) {

        // TEST CODE FOR DISPLAYING DATABASE, DO NOT KEEP
        //Text(database.getAll().toString())

        LazyColumn {
            items(appNames) { app ->
                InstalledAppRow(app, checkedApps)
            }
        }
    }
}


@Composable
fun InstalledAppRow(
    appName: String,
    checkedApps: MutableList<String>
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            // if app is not in checkedApps, the box is not checked
            checked = checkedApps.contains(appName),
            // if the box just got checked, add the app to checkedApps,
            // which will in turn cause checked parameter to be true
            onCheckedChange = { newlyChecked ->
                if (newlyChecked) {
                    checkedApps.add(appName)
                } else {
                    checkedApps.remove(appName)
                }
            }
        )

        Text(appName, fontSize = 15.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(50.dp))
    }
}
