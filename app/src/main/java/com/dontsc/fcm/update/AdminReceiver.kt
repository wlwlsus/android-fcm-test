package com.dontsc.fcm.update

import android.app.admin.DeviceAdminReceiver
import android.widget.Toast

import android.content.Context

import android.content.Intent
import com.dontsc.fcm.R.string.*


class AdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) = Toast.makeText(
        context,
        context.getString(device_admin_enabled),
        Toast.LENGTH_SHORT
    ).show()

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return context.getString(device_admin_warning)
    }

    override fun onDisabled(context: Context, intent: Intent) = Toast.makeText(
        context,
        context.getString(device_admin_disabled),
        Toast.LENGTH_SHORT
    ).show()

    override fun onLockTaskModeEntering(context: Context, intent: Intent, pkg: String) =
        Toast.makeText(context, context.getString(kiosk_mode_enabled), Toast.LENGTH_SHORT)
            .show()

    override fun onLockTaskModeExiting(context: Context, intent: Intent) =
        Toast.makeText(context, context.getString(kiosk_mode_disabled), Toast.LENGTH_SHORT)
            .show()
}