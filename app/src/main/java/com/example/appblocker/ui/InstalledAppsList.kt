package com.example.appblocker.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InstalledAppsList(context: Context, modifier: Modifier = Modifier) {
    val packageManager = context.packageManager
    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    
    val checkedApps = remember { mutableStateListOf<ApplicationInfo>() }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(installedApps) { app ->
            if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                InstalledAppRow(app, packageManager, checkedApps)
            }
        }
    }

    Button(onClick = {
        // Save the list of checked apps
        val sharedPreferences = context.getSharedPreferences("app_blocker", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val packageNames = checkedApps.map { it.packageName }
        editor.putStringSet("checked_apps", packageNames.toSet())
        editor.apply()
    }) {
        Text("Save")
    }
}

@Composable
fun InstalledAppRow(app: ApplicationInfo,
                    packageManager: PackageManager,
                    checkedApps: MutableList<ApplicationInfo>) {
    val appIcon = app.loadIcon(packageManager)
    Row {
        Checkbox(
            checked = checkedApps.contains(app),
            onCheckedChange = { newlyChecked ->
                if (newlyChecked) {
                    checkedApps.add(app)
                } else {
                    checkedApps.remove(app)
                }
            }
        )

        Text(app.loadLabel(packageManager).toString(), fontSize = 15.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(50.dp))
    }
}
