package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val userName : String,
    @SerializedName("nickname")
    val nickName : String ,
    @SerializedName("user_picture")
    val userPicture : String,
    @SerializedName("goal_distance")
    val goalDistance : Float ,
    @SerializedName("crew_id")
    val crewId : Int
)
