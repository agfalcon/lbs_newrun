package kr.ac.kumoh.newrun.data.api

import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.RankResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RankApi {

    @GET("top10_record")
    fun getRank() : Call<List<RankResponse>>
}