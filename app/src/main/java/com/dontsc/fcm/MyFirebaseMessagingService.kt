package com.dontsc.fcm

import android.util.Log
import com.dontsc.fcm.view.MainActivity.Companion.liveTextView
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


open class MyFirebaseMessagingService : FirebaseMessagingService() {

  private val TAG = "MyFirebaseMsgService"

  override fun onNewToken(p0: String) {
    super.onNewToken(p0)

    val refreshedToken: Task<String> = FirebaseMessaging.getInstance().token
    Log.d(TAG, "Refreshed token: $refreshedToken")

    sendRegistrationToServer(refreshedToken)
  }

  private fun sendRegistrationToServer(refreshedToken: Task<String>) {
    Log.e(TAG, "sendRegistrationToServer: $refreshedToken")
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    Log.d(TAG, "From: " + remoteMessage.from)
    if (remoteMessage.data.isNotEmpty()) {
      Log.d(TAG, "Message data payload: " + remoteMessage.data)
    }

    remoteMessage.notification ?: return
    Log.d(TAG, "Message Notification Body: " + remoteMessage.notification?.body)
    liveTextView.postValue(remoteMessage.notification?.body)
  }
}