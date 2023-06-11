package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("image")
    val image: String
)
