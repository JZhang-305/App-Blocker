package com.example.appblocker.backend

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember


fun getBlocklistNames(context: Context): Array<String> {
        val database = context.getSharedPreferences("app_blocklists", Context.MODE_PRIVATE)
        val originalBlocklistNames = database.getAll().keys.toTypedArray()
        originalBlocklistNames.sort()
        return originalBlocklistNames
    }
