package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.DiffusionModel
import kr.ac.kumoh.newrun.data.model.Message
import kr.ac.kumoh.newrun.data.model.RunResultRequest
import kr.ac.kumoh.newrun.data.model.StableRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DiffusionApi {
    @POST("stable")
    fun getImage(
        @Body image: StableRequest
    ): Call<DiffusionModel>

}