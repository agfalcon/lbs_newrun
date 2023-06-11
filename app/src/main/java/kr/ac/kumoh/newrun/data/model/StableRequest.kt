package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class StableRequest(
    @SerializedName("keyword")
    val keyword : String,
    @SerializedName("image")
    val image : String
)
