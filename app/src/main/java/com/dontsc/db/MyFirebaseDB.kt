package com.dontsc.db

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyFirebaseDB {

  private val TAG = "Firebase DB"
  private lateinit var database: DatabaseReference

  fun connDB() {
    val db = Firebase.database
    val myRef = db.getReference("message")
    myRef.setValue("Hello, World!!")

    database = Firebase.database.reference


    val postListener = object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val post = snapshot.value
        Log.e(TAG, "onDataChange: $post")
      }

      override fun onCancelled(error: DatabaseError) {
        Log.e(TAG, "onCancelled: ${error.message}")
      }

    }

    database.addValueEventListener(postListener)
    writeStock("m1", 20)

  }

  private fun writeStock(motor: String, stock: Int) {
    database.child(motor).setValue(stock)
  }


}