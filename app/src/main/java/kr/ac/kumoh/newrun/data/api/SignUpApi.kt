package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.SignUp
import kr.ac.kumoh.newrun.data.model.MessageResponse
import kr.ac.kumoh.newrun.data.model.SocialSignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("sns_join")
    fun snsSignUp(
        @Body socialSignUp: SocialSignUp
    ) : Call<MessageResponse>

    @POST("join")
    fun signUp(
        @Body signUp: SignUp
    ) : Call<MessageResponse>
}