package kr.ac.kumoh.newrun.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TimeRecordData(
    val time: Int,
    val latitude: Double,
    val longitude: Double
) : Parcelable
