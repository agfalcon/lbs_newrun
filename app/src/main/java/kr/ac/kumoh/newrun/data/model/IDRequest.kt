package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class IDRequest (
    @SerializedName("email")
    val userEmail : String
)