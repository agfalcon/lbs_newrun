package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message")
    var message : String
)

