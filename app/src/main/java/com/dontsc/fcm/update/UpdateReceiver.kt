package com.dontsc.fcm.update

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dontsc.fcm.view.MainActivity

class UpdateReceiver : BroadcastReceiver() {
    private val TAG = "Update page"
    override fun onReceive(context: Context, intent: Intent) {
        // Restart your app here
        val i = Intent(context, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
        Log.e(TAG, "onReceive: 업데이트 완료")
    }
}