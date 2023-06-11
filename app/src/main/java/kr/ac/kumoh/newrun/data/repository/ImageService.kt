package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.ImageId

class ImageService {
    suspend fun getImage(id: Int) : String{
        val image = RetrofitService.imageService.getImage(ImageId(id)).execute()
        if(image.isSuccessful){
            return image.body() ?: "에러"
        }
        return "에러"
    }
}