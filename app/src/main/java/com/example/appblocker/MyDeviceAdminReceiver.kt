package com.example.appblocker

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * This is the component that is responsible for actual device administration.
 * It becomes the receiver when a policy is applied. It is important that we
 * subclass DeviceAdminReceiverClass class here and to implement its only required
 * method onEnabled().
 */
class MyDeviceAdminReceiver : DeviceAdminReceiver() {
    /** Called when this application is approved to be a device administrator.  */
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Toast.makeText(
            context, "Device admin is enabled",
            Toast.LENGTH_LONG
        ).show()
        Log.d(TAG, "onEnabled")
    }

    /** Called when this application is no longer the device administrator.  */
    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Toast.makeText(
            context, "Device admin is disabled",
            Toast.LENGTH_LONG
        ).show()
        Log.d(TAG, "onDisabled")
    }

    override fun onPasswordChanged(context: Context, intent: Intent) {
        super.onPasswordChanged(context, intent)
        Log.d(TAG, "onPasswordChanged")
    }

    override fun onPasswordFailed(context: Context, intent: Intent) {
        super.onPasswordFailed(context, intent)
        Log.d(TAG, "onPasswordFailed")
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent) {
        super.onPasswordSucceeded(context, intent)
        Log.d(TAG, "onPasswordSucceeded")
    }

    companion object {
        const val TAG = "DeviceAdminReceiverClass"
    }
}