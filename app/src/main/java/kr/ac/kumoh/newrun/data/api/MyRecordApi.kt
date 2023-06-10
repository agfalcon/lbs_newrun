package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.MyRecord
import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.RunData
import kr.ac.kumoh.newrun.data.model.WeekRecord
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyRecordApi {
    @POST("record")
    fun recordRunning(
        @Body id: IDRequest
    ): Call<MyRecord>

    @POST("dis_run_aver_sp")
    fun weekRecord(
        @Body id: IDRequest
    ): Call<WeekRecord>

    @POST("all_run_record")
    fun getAllRunData(
        @Body id: IDRequest
    ): Call<List<RunData>>
}