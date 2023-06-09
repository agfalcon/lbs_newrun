package kr.ac.kumoh.newrun.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.Message
import kr.ac.kumoh.newrun.data.model.RunResultRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordRunningService {

    suspend fun recordRunning(runResult: RunResultRequest) : Message {
        val result = RetrofitService.recordRunningService.recordRunning(runResult).execute()
        if(result.isSuccessful){
            return result.body() ?: Message("반환 값 없음")
        }
        else {
            return Message("에러 발생")
        }
    }
}