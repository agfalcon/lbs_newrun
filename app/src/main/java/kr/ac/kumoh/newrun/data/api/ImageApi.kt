package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.IDRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageApi {
    @POST("image_make")
    fun getImage(
        @Body id: IDRequest
    ) : Call<String>
}