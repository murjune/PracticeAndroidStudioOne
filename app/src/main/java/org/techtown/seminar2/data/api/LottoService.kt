package org.techtown.seminar2.data.api

import org.techtown.seminar2.data.entry.search.lotto.ResponseLottoNum
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LottoService {
    @GET("common.do")
    suspend fun responseLottoInfo(@Query("drwNo") round: String): Response<ResponseLottoNum>
}