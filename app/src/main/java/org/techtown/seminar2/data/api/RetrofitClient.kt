package org.techtown.seminar2.data.api

import org.techtown.seminar2.util.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofitClient: Retrofit = Retrofit.Builder()
        .baseUrl(API.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val photoService: IRetrofit = retrofitClient.create(IRetrofit::class.java)
}
