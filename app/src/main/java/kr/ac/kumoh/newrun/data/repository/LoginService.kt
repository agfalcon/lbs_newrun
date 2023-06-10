package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.LoginData

class LoginService {
    suspend fun login(loginData: LoginData) : String {
        val result = RetrofitService.loginService.login(loginData).execute()
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