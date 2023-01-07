package com.example.appblocker.backend

import android.app.admin.DevicePolicyManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import com.example.appblocker.MyDeviceAdminReceiver
import java.util.*


/*
fun getForegroundApp(context: Context): String? {
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


fun Block(context: Context, appsToBlock: Array<String>) {
//    val deviceAdminReceiver =

    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

    val componentName = ComponentName(
        context,
        MyDeviceAdminReceiver::class.java
    )
    devicePolicyManager.setPackagesSuspended(componentName, appsToBlock, true)}


//fun Block (context: Context, appsToBlock: Array<String>) {
//    val mActivityManager: ActivityManager? = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
//    val runningTask: List<ActivityManager.RunningTaskInfo>? = mActivityManager?.getRunningTasks(1)
//    val ar: ActivityManager.RunningTaskInfo? = runningTask?.get(0)
//    val activityOnTop = ar?.topActivity?.className
//
//    val startHomescreen = Intent(Intent.ACTION_MAIN)
//    startHomescreen.addCategory(Intent.CATEGORY_HOME)
//    startHomescreen.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//    context.startActivity(startHomescreen)
//}
*/
