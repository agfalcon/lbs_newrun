package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class SignUp(
    @SerializedName("email")
    val email : String,
    @SerializedName("username")
    val userName : String,
    @SerializedName("nickname")
    val nickName : String,
    @SerializedName("password")
    val password: String,
    @SerializedName("goal_distance")
    val goal_Distance : Float,
)
