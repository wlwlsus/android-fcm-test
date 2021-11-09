package com.dontsc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dontsc.db.MyFirebaseDB
import com.dontsc.fcm.R

class DatabaseActivity : AppCompatActivity() {
  private lateinit var btn1: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_database)

    btn1 = findViewById(R.id.goHome)

    btn1.setOnClickListener {
      startActivity(Intent(this, MainActivity::class.java))
    }

    val db = MyFirebaseDB()
    db.connDB()
  }
}