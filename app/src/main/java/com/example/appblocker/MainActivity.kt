package com.example.appblocker

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // uncomment to activate AppCheckingWorker.kt
        /*
        val workRequest = OneTimeWorkRequestBuilder<AppCheckingWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        */

        val REQUEST_CODE_ENABLE_ADMIN = 1
        var devicePolicyManager: DevicePolicyManager? = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        var deviceAdmin: ComponentName? = ComponentName(this, MyDeviceAdminReceiver::class.java)
        // Launch the activity to have the user enable our admin.

        if (devicePolicyManager!!.isAdminActive(deviceAdmin!!)) {
        }
        else {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin)
            }
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)


        setContent {
            Navigation(this)

        }



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
    //intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION")

    context.startActivity(intent)
}


