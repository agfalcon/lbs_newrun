package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class WeekRecord(
    @SerializedName("totalDistance")
    val totalDistance : Float,
    @SerializedName("avgSpeed")
    val avgSpeed : Float
)
