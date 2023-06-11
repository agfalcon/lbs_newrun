package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.ImageId
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageApi {
    @POST("image_make")
    fun getImage(
        @Body id: ImageId
    ) : Call<String>
}