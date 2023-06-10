package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.SignUp
import kr.ac.kumoh.newrun.data.model.SocialSignUp

class SignUpService {
    suspend fun snsSignUp(socialSignUp: SocialSignUp) : String {
        val result = RetrofitService.signUpService.snsSignUp(socialSignUp).execute()
        if(result.isSuccessful){
            if(result.body()?.message=="")
                return result.body()?.error ?: "Unknown error"
            else
                return result.body()?.message ?: "Unknown error"
        }
        else{
            return "통신 장애. 잠시후 시도해주세요"
        }
    }

    suspend fun signUp(signUp: SignUp) : String {
        val result = RetrofitService.signUpService.signUp(signUp).execute()
        if(result.isSuccessful){
            if(result.body()?.message=="")
                return result.body()?.error ?: "Unknown error"
            else
                return result.body()?.message ?: "Unknown error"
        }
        else{
            return "통신 장애. 잠시후 시도해주세요"
        }
    }
}