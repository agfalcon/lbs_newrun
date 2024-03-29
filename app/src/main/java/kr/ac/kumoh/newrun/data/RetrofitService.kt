package kr.ac.kumoh.newrun.data

import com.google.gson.GsonBuilder
import kr.ac.kumoh.newrun.data.api.DiffusionApi
import kr.ac.kumoh.newrun.data.api.ImageApi
import kr.ac.kumoh.newrun.data.api.LoginApi
import kr.ac.kumoh.newrun.data.api.MyRecordApi
import kr.ac.kumoh.newrun.data.api.RankApi
import kr.ac.kumoh.newrun.data.api.RecordRunningApi
import kr.ac.kumoh.newrun.data.api.SignUpApi
import kr.ac.kumoh.newrun.data.api.UserApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


object RetrofitService {
    //Node.js와 통신
    val gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder().baseUrl("https://newrun-lbs.run.goorm.site/")
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    val recordRunningService = retrofit.create(RecordRunningApi::class.java)
    val myRecordService = retrofit.create(MyRecordApi::class.java)
    val userService = retrofit.create(UserApi::class.java)
    val rankService = retrofit.create(RankApi::class.java)
    val signUpService = retrofit.create(SignUpApi::class.java)
    val loginService = retrofit.create(LoginApi::class.java)
    val imageService = retrofit.create(ImageApi::class.java)

    //Flask 서버와 통신

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit2 = Retrofit.Builder().baseUrl("http://10.0.2.2:5000/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val diffusionService = retrofit2.create(DiffusionApi::class.java)

}