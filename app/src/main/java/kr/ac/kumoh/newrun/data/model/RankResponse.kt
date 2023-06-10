package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class RankResponse(
    @SerializedName("username")
    val userName : String,
    @SerializedName("nickname")
    val nickName : String,
    @SerializedName("totalDistance")
    val totalDistance : Float,
)
