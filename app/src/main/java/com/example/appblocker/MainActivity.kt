package com.example.appblocker

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat.startActivityForResult


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // uncomment to activate AppCheckingWorker.kt
        /*
        val workRequest = OneTimeWorkRequestBuilder<AppCheckingWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        */

        var devicePolicyManager: DevicePolicyManager? = null
        var deviceAdmin: ComponentName? = null



        setContent {
            Navigation(this)
        }


    }
}

private fun checkAndTakeUserToEnableAdminApp(context: Context) {
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val deviceAdmin = ComponentName(context, MyDeviceAdminReceiver::class.java)
    if (devicePolicyManager!!.isAdminActive(deviceAdmin!!)) {

    } else {
        showDeviceAdminPopup(context = context, deviceAdmin = deviceAdmin)
    }

}

fun showDeviceAdminPopup(context: Context, deviceAdmin: ComponentName) {
    val REQUEST_CODE_ENABLE_ADMIN = 1;
    val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin)
    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION")

    context.startActivity(intent)
}

