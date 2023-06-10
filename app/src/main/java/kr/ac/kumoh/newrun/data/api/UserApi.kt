package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("user")
    fun getUserInfo(@Body id : IDRequest) : Call<UserResponse>
}