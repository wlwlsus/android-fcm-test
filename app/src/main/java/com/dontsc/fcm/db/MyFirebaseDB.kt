package com.dontsc.fcm.db

import android.util.Log
import com.google.firebase.database.*

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

    /**
     * addValueEventListener 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기한다. - 값이 바뀔 때 마다 onDataChange 호출
     *
     * addListenerForSingleValueEvent : 한 번 호출된 후 다시 호출 되지 않는다. - 1회성
     * 한 번만 호출되고 즉시 삭제되는 콜백이 필요한 경우에 사용한다.
     * 한 번 로드된 후 자주 변경되지 않거나 능동적으로 수신 대기할 필요가 없는 데이터에 유용하다.
     *
     * addChildEventListener : 데이터베이스의 특정한 노드에 대한 변경을 수신 대기하는데 유용하다.
     * 목록을 다루는 앱은 단일 개체에 사용되는 값 이벤트보다는 하위 이벤트를 수신 대기해야한다.
     * 하위 하목에 push() 메소드를 통해 새로 추가되거나 updateChildren() 메소드를 통해 업데이트 되는 경우가 그 예이다.
     *
     * DB 읽기 동작의 오버헤드를 줄이고 성능을 높이기 위해서 용도에 맞게 구현해야한다.
     */
    fun readStock(id: String) {
        mDatabase.child("kiosk_id").child(id).child("motors")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e(TAG, "onDataChange: 변화")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled : $error ")
                }
            })
    }
}