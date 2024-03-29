package kr.ac.kumoh.newrun.data.repository

import android.util.Log
import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.DiffusionModel
import kr.ac.kumoh.newrun.data.model.Message
import kr.ac.kumoh.newrun.data.model.RunResultRequest
import kr.ac.kumoh.newrun.data.model.StableRequest

class StableDiffusionService {
    suspend fun getImage(keyword: String, image: String) : DiffusionModel {
        val result = RetrofitService.diffusionService.getImage(StableRequest( keyword, image)).execute()
        Log.d("테스트", "result: ${result}")
        if(result.isSuccessful){
            return result.body() ?: DiffusionModel("반환 값 없음")
        }
        else {
            return DiffusionModel("에러 발생")
        }
    }
}