package kr.ac.kumoh.newrun.data

import kr.ac.kumoh.newrun.data.api.RecordRunningApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    val retrofit = Retrofit.Builder().baseUrl("https://newrun-lbs.run.goorm.site/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val recordRunningService = retrofit.create(RecordRunningApi::class.java)

}