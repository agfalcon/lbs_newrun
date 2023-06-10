package kr.ac.kumoh.newrun.data.repository

import com.kizitonwose.calendar.core.Week
import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.MyRecord
import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.RunData
import kr.ac.kumoh.newrun.data.model.WeekRecord

class MyRecordService {
    suspend fun getMyRecord(id: String) : MyRecord {
        val result = RetrofitService.myRecordService.recordRunning(IDRequest(id = id)).execute()
        if(result.isSuccessful){
            return result.body() ?: MyRecord(0.0,0.0,0.0)
        }
        else {
            return MyRecord(0.0,0.0,0.0)
        }
    }

    suspend fun weekRecord(id: String) : WeekRecord {
        val result = RetrofitService.myRecordService.weekRecord(IDRequest(id = id)).execute()
        if(result.isSuccessful){
            return result.body() ?: WeekRecord(0.0f,0.0f)
        }
        else {
            return WeekRecord(0.0f,0.0f)
        }
    }

    suspend fun getAllRunData(id: String) : List<RunData> {
        val result = RetrofitService.myRecordService.getAllRunData(IDRequest(id= id)).execute()
        if(result.isSuccessful){
            return result.body() ?: emptyList()
        }
        else {
            return emptyList()
        }
    }
}