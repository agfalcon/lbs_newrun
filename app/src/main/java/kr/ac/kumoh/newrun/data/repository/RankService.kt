package kr.ac.kumoh.newrun.data.repository

import kr.ac.kumoh.newrun.data.RetrofitService
import kr.ac.kumoh.newrun.data.model.IDRequest
import kr.ac.kumoh.newrun.data.model.RankResponse

class RankService {

    suspend fun getRank(id: String) : List<RankResponse>{
        val result = RetrofitService.rankService.getRank(IDRequest(id)).execute()
        if(result.isSuccessful){
            return result.body() ?: emptyList()
        }
        return emptyList()
    }
}