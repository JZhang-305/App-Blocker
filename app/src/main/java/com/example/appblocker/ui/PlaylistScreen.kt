package com.example.appblocker.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PlaylistScreen(context: Context, navController: NavHostController, modifier: Modifier = Modifier) {
    val database = context.getSharedPreferences("app_playlists", Context.MODE_PRIVATE)
    // Scaffold, bottomBar, and BottomAppBar need to be coupled to create a persistent bottom bar
    Scaffold(
        bottomBar = {
            BottomAppBar {
                // When clicked, button should navigate to PlaylistSelectorScreen to make a new playlist
                Button(
                    onClick = {
                        navController.navigate("PlaylistSelectorScreen")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
                ) {
                    Text("New", color = Color.Black)
                }
            }
        }
    ) {

        val playlistNames = database.getAll().keys.toList()
        LazyColumn {
            items(playlistNames) { playlist ->
                //Divider(color = Color.Gray)
                PlaylistRow(playlist = playlist, database = database)
                Divider(color = Color.Gray, )
            }
        }
    }
}

@Composable
fun PlaylistRow(playlist: String, database: SharedPreferences, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(playlist, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(50.dp))
        Button(onClick = {expanded = true}) {Text("Apps")}
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (appInPlaylist in database.getStringSet(playlist, mutableSetOf())?.toList()?.sorted()!!) {
                DropdownMenuItem(onClick = {}) {
                    Text(appInPlaylist)
                }
                Divider()
            }
        }

        // Insert garbage can button that deletes
        Spacer(modifier = Modifier.height(50.dp))
    }
}
