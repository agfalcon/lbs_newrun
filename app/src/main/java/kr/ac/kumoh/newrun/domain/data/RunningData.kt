package kr.ac.kumoh.newrun.domain.data

import kr.ac.kumoh.newrun.domain.model.TimeRecordData
import java.util.Timer

object RunningData {
    var timer: Timer? = null
    var timeRecord = mutableListOf<TimeRecordData>()
    var time = 0
    var distance:Double = 0.0
    var velocity:Double = 0.0
    val R = 6372.8 * 1000

    fun reset() {
        timer = null
        timeRecord = mutableListOf<TimeRecordData>()
        time = 0
        distance = 0.0
        velocity = 0.0
    }

}

