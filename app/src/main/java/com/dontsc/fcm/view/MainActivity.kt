package com.dontsc.fcm.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.dontsc.fcm.R
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    companion object {
        var liveTextView: MutableLiveData<String> = MutableLiveData("Default Value")
    }

    private val TAG = "Main"

    private var count = 0
    private lateinit var textView: TextView
    private lateinit var btn: Button
    private lateinit var dbBtn: Button
    private lateinit var autoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView01)
        btn = findViewById(R.id.button)
        dbBtn = findViewById(R.id.db)
        autoButton = findViewById(R.id.auto_install)

        btn.text = count.toString()

        btn.setOnClickListener {
            count++
            btn.text = count.toString()
        }

        liveTextView.observe(this, { code ->
//      textView.text = code

            val inst = code.split(" ")

            when (inst[0]) {
                "stop" -> {
                    if (count == inst[1].toInt()) {
                        Toast.makeText(this, "$count 번 키오스크 이벤트", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.e(TAG, "onCreate: $token")

            // Log and toast
            val msg = "MSG Token"
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }

        dbBtn.setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        autoButton.setOnClickListener {
            startActivity(Intent(this, InstallActivity::class.java))
        }

    }
}