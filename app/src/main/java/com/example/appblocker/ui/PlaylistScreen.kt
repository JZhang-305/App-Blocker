package com.example.appblocker.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appblocker.R

@Composable
fun PlaylistScreen(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val database = context.getSharedPreferences("app_playlists", Context.MODE_PRIVATE)
    val originalPlaylistNames = database.getAll().keys.toTypedArray()
    originalPlaylistNames.sort()
    val playlistNames = remember { mutableStateListOf<String>(*originalPlaylistNames) }
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
        Column {
            Spacer(modifier = modifier.height(15.dp))
            Text(
                "App Playlists",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = modifier.height(15.dp))
            Divider(color = Color.Gray)
            var counter: Int = 0
            LazyColumn {
                items(playlistNames) { playlist ->
                    //Divider(color = Color.Gray)
                    PlaylistRow(
                        playlist = playlist,
                        database = database,
                        counter = counter,
                        numOfPlaylists = playlistNames.size,
                        onPlaylistRemoved = {
                            playlistNames.remove(playlist)
                        }
                    )


                    counter++
                }
            }

        }
    }
}

@Composable
fun PlaylistRow(
    playlist: String,
    database: SharedPreferences,
    counter: Int,
    numOfPlaylists: Int,
    onPlaylistRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                playlist,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(top = 7.dp, start = 7.dp)
                    .clickable { expanded = true })
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (appInPlaylist in database.getStringSet(playlist, mutableSetOf())?.toList()
                    ?.sorted()!!) {
                    DropdownMenuItem(onClick = {}) {
                        Text(appInPlaylist)
                    }
                    Divider()
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.trash_can),
            contentDescription = "Cart button icon",
            modifier = modifier
                .padding(all = 7.dp)
                .align(Alignment.End)
                .clickable {
                    with(database.edit()) {
                        remove(playlist)
                        apply()
                    }
                    onPlaylistRemoved()
                }
        )

        //Row(verticalAlignment = Alignment.CenterVertically) {
        //Row() {
        //Button(onClick = { expanded = true }) { Text("Apps") }


        //Spacer(modifier = Modifier.width(100.dp))


    }
    Divider(color = Color.Gray)

    // }
    if (counter == numOfPlaylists - 1) {
        Spacer(modifier = Modifier.height(100.dp))
    }
    //Spacer(modifier = Modifier.height(50.dp))
}
