package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class RunResultRequest(
    @SerializedName("distance")
    val distance : String,
    @SerializedName("speed")
    val speed : String,
    @SerializedName("run_time")
    val run_time : String,
    @SerializedName("route")
    val route : String,
    @SerializedName("user_id")
    val userId : String,
)
