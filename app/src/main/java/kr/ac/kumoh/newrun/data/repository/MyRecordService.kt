package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.Message
import kr.ac.kumoh.newrun.data.model.MyRecord
import kr.ac.kumoh.newrun.data.model.RecordRequest
import kr.ac.kumoh.newrun.data.model.RunResultRequest

class MyRecordService {
    suspend fun getMyRecord(id: String) : MyRecord {
        val result = RetrofitService.myRecordService.recordRunning(RecordRequest(id = id)).execute()
        if(result.isSuccessful){
            return result.body() ?: MyRecord(0.0,0.0,0.0)
        }
        else {
            return MyRecord(0.0,0.0,0.0)
        }
    }
}