package com.dontsc.data.model


import com.google.gson.annotations.SerializedName

data class Motors(
    @SerializedName("m1")
    val m1: Int,
    @SerializedName("m2")
    val m2: Int,
    @SerializedName("m3")
    val m3: Int,
    @SerializedName("m4")
    val m4: Int,
    @SerializedName("m5")
    val m5: Int,
    @SerializedName("m6")
    val m6: Int
)