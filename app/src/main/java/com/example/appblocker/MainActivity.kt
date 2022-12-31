package com.example.appblocker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appblocker.backend.AppCheckingWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val workRequest = OneTimeWorkRequestBuilder<AppCheckingWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)


        setContent {
            Navigation(this)
        }


    }
}
