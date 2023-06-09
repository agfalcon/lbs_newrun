package kr.ac.kumoh.newrun.data

import kr.ac.kumoh.newrun.data.api.DiffusionApi
import kr.ac.kumoh.newrun.data.api.MyRecordApi
import kr.ac.kumoh.newrun.data.api.RecordRunningApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    //Node.js와 통신
    val retrofit = Retrofit.Builder().baseUrl("https://newrun-lbs.run.goorm.site/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val recordRunningService = retrofit.create(RecordRunningApi::class.java)
    val myRecordService = retrofit.create(MyRecordApi::class.java)


    //Flask 서버와 통신
    val retrofit2 = Retrofit.Builder().baseUrl("http://10.0.2.2:5000/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val diffusionService = retrofit2.create(DiffusionApi::class.java)

}