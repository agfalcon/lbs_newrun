package kr.ac.kumoh.newrun.data.repository

import com.kakao.sdk.user.model.User
import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.UserResponse
import kr.ac.kumoh.newrun.domain.data.UserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserService {

    suspend fun getUserInfo(id: String) {
        val result = RetrofitService.userService.getUserInfo(IDRequest(UserInfo.id.toString())).execute()
        if(result.isSuccessful){
            val info = result.body()
            UserInfo.id = info?.id ?: 0
            UserInfo.userName  = info?.userName ?: ""
            UserInfo.nickName = info?.nickName ?: ""
            UserInfo.goalDistance = info?.goalDistance ?: 0f
            UserInfo.userPicture = info?.userPicture ?: ""
            UserInfo.crewId = info?.crewId ?: 0
        }
    }
}