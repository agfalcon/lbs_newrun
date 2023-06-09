package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.Message
import kr.ac.kumoh.newrun.data.model.RunResultRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RecordRunningApi {

    @POST("run")
    fun recordRunning(
        @Body runResultRequest: RunResultRequest
    ): Call<Message>
}