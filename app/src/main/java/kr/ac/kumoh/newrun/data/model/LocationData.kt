package kr.ac.kumoh.newrun.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationData(
    val x: Float,
    val y: Float
) :Parcelable

