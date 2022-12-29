package com.example.appblocker.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun PlaylistSelectorScreen(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val packageManager = context.packageManager
    val database = context.getSharedPreferences("app_playlists", Context.MODE_PRIVATE)
    // current set playlist name in TextField
    var playlistName: String by remember { mutableStateOf("Name of Playlist") }
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
                    value = playlistName,
                    onValueChange = {
                        playlistName = it
                    },
                    singleLine = true,
                    modifier = Modifier.widthIn(0.dp, 250.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // When clicked, button should save a new playlist and navigate back to screen displaying all playlists
                Button(
                    onClick = {


                        // code to add key-value pair to database <playlistName, apps in said playlist>
                        with(database.edit()) {
                            putStringSet(playlistName, checkedApps.toMutableSet())
                            apply()
                        }
                        navController.navigate("PlaylistScreen")
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
