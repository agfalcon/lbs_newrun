package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class SocialSignUp(
    @SerializedName("email")
    val email : String,
    @SerializedName("username")
    val userMame : String,
    @SerializedName("nickname")
    val nickName : String,
    @SerializedName("goal_distance")
    val goal_Distance : Float,
)