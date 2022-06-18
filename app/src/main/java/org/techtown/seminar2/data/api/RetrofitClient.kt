package org.techtown.seminar2.data.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import org.techtown.seminar2.util.API
import org.techtown.seminar2.util.Constants.TAG
import org.techtown.seminar2.util.isJsonArray
import org.techtown.seminar2.util.isJsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object RetrofitClient {
    private val retrofitClient: Retrofit = Retrofit.Builder()
        .baseUrl(API.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        // (추가) 6. 레트로핏 클라이언트 추가
        .client(getHttpClient())
        .build()

    // 1) 로깅 인터셉터
    private fun getHttpClient(): OkHttpClient {
        // 1. okhttp 인스턴스
        val client = OkHttpClient.Builder()
        // 2. 로그 찍기위한 로깅 인터셉터
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, "RetrofitClient - log() called/ message: $message")
                when {
                    message.isJsonObject() ->
                        Log.d(TAG, JSONObject(message).toString(4))
                    message.isJsonArray() ->
                        Log.d(TAG, JSONArray(message).toString(4))
                    else ->
                        try {
                            Log.d(TAG, JSONObject(message).toString(4))
                        } catch (e: Exception) {
                            Log.d(TAG, message)
                        }
                }
            }
        })

        // 3. loggingInterceptor의 level설정
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        // 4. client에 로깅인터셉터 추가
        client.addInterceptor(loggingInterceptor)
        // 5. OKHttpClient 객체 반환
        return client.build()
    }

    val photoService: IRetrofit = retrofitClient.create(IRetrofit::class.java)
}
