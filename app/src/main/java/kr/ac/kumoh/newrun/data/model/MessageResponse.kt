package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    val message : String = "",
    @SerializedName("error")
    val error: String = ""
)
