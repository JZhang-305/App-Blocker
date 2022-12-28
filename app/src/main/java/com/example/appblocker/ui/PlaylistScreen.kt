package com.example.appblocker.ui

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
                // When clicked, button should save a new playlist and navigate back to screen displaying all playlists
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

        // TEST CODE FOR DISPLAYING DATABASE, DO NOT KEEP
        //Text(database.getAll().toString())

        val playlistNames = database.getAll().keys.toList()
        LazyColumn {
            items(playlistNames) { playlist ->
                Text(playlist, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}