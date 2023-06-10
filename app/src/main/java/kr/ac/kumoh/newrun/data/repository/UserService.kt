package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.domain.data.UserInfo


class UserService {

    suspend fun getUserInfo(id: String) {
        val result = RetrofitService.userService.getUserInfo(IDRequest(UserInfo.userEmail.toString())).execute()
        if(result.isSuccessful){
            val info = result.body()
            UserInfo.userEmail = info?.id ?: 0
            UserInfo.userName  = info?.userName ?: ""
            UserInfo.nickName = info?.nickName ?: ""
            UserInfo.goalDistance = info?.goalDistance ?: 0f
            UserInfo.userPicture = info?.userPicture ?: ""
            UserInfo.crewId = info?.crewId ?: 0
        }
    }
}