package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.IDRequest

class ImageService {
    suspend fun getImage(email: String) : String{
        val image = RetrofitService.imageService.getImage(IDRequest(email)).execute()
        if(image.isSuccessful){
            return image.body() ?: "에러"
        }
        return "에러"
    }
}