package kr.ac.kumoh.newrun.data.api

import com.google.firebase.auth.UserInfo
import kr.ac.kumoh.newrun.data.model.Message
import kr.ac.kumoh.newrun.data.model.MyRecord
import kr.ac.kumoh.newrun.data.model.RecordRequest
import kr.ac.kumoh.newrun.data.model.RunResultRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyRecordApi {
    @POST("record")
    fun recordRunning(
        @Body id: RecordRequest
    ): Call<MyRecord>
}