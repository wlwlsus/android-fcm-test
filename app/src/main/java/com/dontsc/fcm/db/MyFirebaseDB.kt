package com.dontsc.fcm.db

import android.util.Log
import com.dontsc.fcm.data.model.M1
import com.dontsc.fcm.data.model.Motors
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap

object MyFirebaseDB {

  private val TAG = "Firebase DB"
  var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

  fun writeStock(id: String, motor: String, stock: Int) {
    mDatabase.child("kiosk_id").child(id).child("motors").child(motor).child("stock")
      .setValue(stock)
      .addOnSuccessListener {
        Log.e(TAG, "writeStock: 저장 성공")
      }
      .addOnFailureListener {
        Log.e(TAG, "writeStock: 저장 실패 $it")
      }
  }

  fun readStock(id: String) {
    mDatabase.child("kiosk_id").child(id).child("motors")
      .addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          Log.e(TAG, "snapshot : $snapshot")
          val post = snapshot.children.forEach {
            Log.e(TAG, "foreach 데이터!: ${it.value}")
          }

//          Log.e(TAG, "onDataChange: $post")
        }

        override fun onCancelled(error: DatabaseError) {
          Log.e(TAG, "onCancelled : $error ")
        }
      })
  }
}