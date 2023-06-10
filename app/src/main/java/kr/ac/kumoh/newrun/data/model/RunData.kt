package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class RunData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("distance")
    val distance: Float,
    @SerializedName("speed")
    val speed: Float,
    @SerializedName("run_time")
    val runTime: String,
    @SerializedName("route")
    val route: List<LocationData>,
    @SerializedName("user_id")
    val userId: Int,

)
