package kr.ac.kumoh.newrun.data.model

import com.google.gson.annotations.SerializedName

data class DiffusionModel(
    @SerializedName("image")
    val image : String
)
