package com.example.appblocker.backend

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember


fun getPlaylistNames(context: Context): Array<String> {
        val database = context.getSharedPreferences("app_playlists", Context.MODE_PRIVATE)
        val originalPlaylistNames = database.getAll().keys.toTypedArray()
        originalPlaylistNames.sort()
        return originalPlaylistNames
    }
