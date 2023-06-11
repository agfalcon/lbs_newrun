package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class snsLoginData(
    @SerializedName("email")
    val email: String
)
