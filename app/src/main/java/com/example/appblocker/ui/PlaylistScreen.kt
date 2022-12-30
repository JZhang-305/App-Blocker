package com.example.appblocker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
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
import com.example.appblocker.backend.getPlaylistNames


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlaylistScreen(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val database = context.getSharedPreferences("app_playlists", Context.MODE_PRIVATE)
    val playlistNames = remember { mutableStateListOf<String>(*getPlaylistNames(context)) }
    val contextForToast = LocalContext.current.applicationContext
    // Scaffold and FAB need to be coupled to create a persistent button
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 16.dp),
                    //.align(alignment = Alignment.BottomEnd),
                onClick = {
                    navController.navigate("PlaylistSelectorScreen")
                }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }

    ) {
        Column {
            Spacer(modifier = modifier.height(15.dp))
            Text(
                "App Playlists",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = modifier.height(15.dp))
            Divider(color = Color.Gray)
            //var counter: Int = 0
            LazyColumn {
                items(playlistNames) { playlist ->
                    PlaylistRow(
                        playlist = playlist,
                        database = database,
                        onPlaylistRemoved = {
                            playlistNames.remove(playlist)

                        }
                    )


                }
            }
        }
    }
}

@Composable
fun PlaylistRow(
    playlist: String,
    database: SharedPreferences,
    onPlaylistRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                playlist,
                fontSize = 30.sp,
                //fontWeight = FontWeight.Bold,
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


    }
    Divider(color = Color.Gray)
}
