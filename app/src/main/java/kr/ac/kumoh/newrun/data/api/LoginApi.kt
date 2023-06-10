package kr.ac.kumoh.newrun.data.api

import com.google.gson.annotations.SerializedName
import kr.ac.kumoh.newrun.data.model.LoginData
import kr.ac.kumoh.newrun.data.model.MessageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    fun login(
        @Body loginData: LoginData
    ) : Call<MessageResponse>
}