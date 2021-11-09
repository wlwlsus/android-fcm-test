package com.dontsc.fcm.data.model


import com.google.gson.annotations.SerializedName


data class Motors(
  @SerializedName("m1")
  val m1: M1,
  @SerializedName("m2")
  val m2: M2,
  @SerializedName("m3")
  val m3: M3,
  @SerializedName("m4")
  val m4: M4,
  @SerializedName("m5")
  val m5: M5,
  @SerializedName("m6")
  val m6: M6
)