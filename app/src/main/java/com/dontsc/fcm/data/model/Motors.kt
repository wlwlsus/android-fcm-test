package com.dontsc.fcm.data.model


import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

/**
 * @IgnoreExtraProperties : firebase 와 비교해서 알맞는 것만 적용시킨다.
 * Properties that don't map to class fields are ignored when serializing to a class annotated with this annotation.
 *
 * @Exclude
 * Marks a field as excluded from the Database.
 */
@IgnoreExtraProperties
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
){

}