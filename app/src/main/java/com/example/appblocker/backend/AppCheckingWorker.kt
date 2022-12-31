package com.example.appblocker.backend

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*


fun getBlockList(context: Context): List<String>? {
    val calendar = Calendar.getInstance()
    val dayNum = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val days: List<DayOfTheWeek>? = loadDaysFromPreferences(context)
    if (days != null) {
        return days[dayNum].appsInBlocklist
    }
    return null
}

class AppCheckingWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val blocklist = getBlockList(applicationContext)
        val currentApp = getForeground(applicationContext)
        currentApp?.let { Log.d("ForegroundStatus", it) }
        if (blocklist != null) {
            if (blocklist.contains(currentApp)){
                showHomeScreen(applicationContext)
                currentApp?.let { Log.d("ForegroundStatus", "making it out the hood") }
            }

        }
        showHomeScreen(applicationContext)
        val refreshWork = OneTimeWorkRequest.Builder(AppCheckingWorker::class.java).build()
        Thread.sleep(10000)
        WorkManager.getInstance(applicationContext).enqueue(refreshWork)
        return Result.success()
    }
}

fun showHomeScreen(context: Context): Boolean {
    val startMain = Intent(Intent.ACTION_MAIN)
    startMain.addCategory(Intent.CATEGORY_HOME)
    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(startMain)
    return true
}

fun loadDaysFromPreferences(context: Context): List<DayOfTheWeek>? {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    if (sharedPreferences.getString("days", null) == null) {
        return null
    }
    return Json.decodeFromString<List<DayOfTheWeek>>(sharedPreferences.getString("days", null)!!)
}

fun saveDaysToPreferences(context: Context, days: List<DayOfTheWeek>) {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val json = Json.encodeToString(days)
    editor.putString("days", json)
    editor.apply()
}

fun getForeground(context: Context): String? {
    var currentApp = "NULL"
    val usm: UsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    val appList =
        usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
    if (appList != null && appList.size > 0) {
        val mySortedMap: SortedMap<Long, UsageStats> = TreeMap<Long, UsageStats>()
        for (usageStats in appList) {
            mySortedMap[usageStats.lastTimeUsed] = usageStats
        }
        if (!mySortedMap.isEmpty()) {
            currentApp = mySortedMap[mySortedMap.lastKey()]?.packageName.toString()
        }
    }
    return currentApp
}