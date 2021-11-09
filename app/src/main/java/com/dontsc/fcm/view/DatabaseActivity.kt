package com.dontsc.fcm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dontsc.fcm.R
import com.dontsc.fcm.db.MyFirebaseDB.mDatabase
import com.dontsc.fcm.db.MyFirebaseDB.readStock
import com.dontsc.fcm.db.MyFirebaseDB.writeStock
import com.google.firebase.database.*

class DatabaseActivity : AppCompatActivity() {

  private val TAG = "Firebase DB"

  //  private lateinit var mDatabase: DatabaseReference
  private lateinit var btn1: Button
  private lateinit var saveBtn: Button
  private lateinit var readBtn: Button
  private lateinit var initial: Button
  private lateinit var motorText: EditText
  private lateinit var stockText: EditText
  private lateinit var idText: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_database)

//    mDatabase = FirebaseDatabase.getInstance().reference

    idText = findViewById(R.id.id_text_input)
    btn1 = findViewById(R.id.goHome)
    saveBtn = findViewById(R.id.save_btn)
    readBtn = findViewById(R.id.read_btn)
    initial = findViewById(R.id.initialization)
    motorText = findViewById(R.id.motor_no)
    stockText = findViewById(R.id.stock_value)

    btn1.setOnClickListener {
      startActivity(Intent(this, MainActivity::class.java))
    }

    saveBtn.setOnClickListener {
      if (idText.text.isBlank()) {
        showToast()
        return@setOnClickListener
      }
      if (motorText.text.isBlank()) {
        showToast()
        return@setOnClickListener
      }
      stockText.text.toString().toBigIntegerOrNull() ?: run {
        showToast()
        return@setOnClickListener
      }

      writeStock(
        idText.text.toString(),
        motorText.text.toString(),
        stockText.text.toString().toInt()
      )
    }

    initial.setOnClickListener {
      if (idText.text.isBlank()) {
        showToast()
        return@setOnClickListener
      }
      for (i in 1..6) {
        writeStock(idText.text.toString(), "m$i", 0)
      }
    }

    readBtn.setOnClickListener {
      if (idText.text.isBlank()) {
        showToast()
        return@setOnClickListener
      }
      readStock(idText.text.toString())
    }
  }

  private fun showToast() {
    Toast.makeText(this@DatabaseActivity, "올바른 값을 입력해주세요.", Toast.LENGTH_SHORT).show()
  }

}