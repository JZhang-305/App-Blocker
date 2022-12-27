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


@Composable
fun InstalledAppsList(context: Context, modifier: Modifier = Modifier) {




    val packageManager = context.packageManager
    val listOfApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    // For speeding up process of displaying all apps
    val installedApps = mutableMapOf<String, ApplicationInfo>()
    for (app in listOfApps) {
        if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
            installedApps[app.loadLabel(packageManager).toString()] = app
        }
    }
    val listOfAppNames = mutableListOf<String>()
    for ((key, value) in installedApps) {
        listOfAppNames.add(key)
    }

    val checkedApps = remember { mutableStateListOf<String>() }

    //val checkedApps = remember { mutableStateMapOf<ApplicationInfo>() }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        // Save the list of checked apps

                        /*
                        val sharedPreferences = context.getSharedPreferences("app_blocker", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val packageNames = checkedApps.map { it.packageName }
                        editor.putStringSet("checked_apps", packageNames.toSet())
                        editor.apply()*/

                    }) {
                    Text("+")
                    for (app in checkedApps) {
                        //Text(i.loadLabel(packageManager).toString())
                        Text(app)
                    }
                }
            }
        }
    ) {

        LazyColumn {
            items(listOfAppNames) { app ->
                InstalledAppRow(app, packageManager, checkedApps)

            }
        }
    }

}


@Composable
fun InstalledAppRow(appName: String,
                    packageManager: PackageManager,
                    checkedApps: MutableList<String>) {
    // val appIcon = app.loadIcon(packageManager)
    //val appName = app.loadLabel(packageManager).toString()
    Row (verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checkedApps.contains(appName),
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
