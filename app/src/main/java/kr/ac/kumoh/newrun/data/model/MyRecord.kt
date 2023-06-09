package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class MyRecord(
    @SerializedName("totalDistance")
    val totalDistance : Double,
    @SerializedName("monthlyDistance")
    val monthlyDistance : Double,
    @SerializedName("weeklyDistance")
    val weeklyDistance : Double,
)
