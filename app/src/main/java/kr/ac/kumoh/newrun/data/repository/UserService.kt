package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.domain.data.UserInfo


class UserService {

    suspend fun getUserInfo(email: String){
        val result = RetrofitService.userService.getUserInfo(IDRequest(email)).execute()
        if(result.isSuccessful){
            val info = result.body()
            UserInfo.userEmail = info?.email ?: ""
            UserInfo.userName  = info?.userName ?: ""
            UserInfo.nickName = info?.nickName ?: ""
            UserInfo.goalDistance = info?.goalDistance ?: 0f
            UserInfo.userPicture = info?.userPicture ?: ""
            UserInfo.crewId = info?.crewId ?: 0
        }
    }
}