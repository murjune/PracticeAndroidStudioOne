package org.techtown.seminar2.data.api

import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import org.techtown.seminar2.util.API
import org.techtown.seminar2.util.isJsonArray
import org.techtown.seminar2.util.isJsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LottoClient {
    const val TAG = "lotto"
    private val lottoClient: Retrofit = Retrofit.Builder()
        .baseUrl(API.LOTO_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getHttpClient())
        .build()

    private fun getHttpClient(): OkHttpClient {

        val client = OkHttpClient.Builder()
        // 1) 로깅 인터셉터
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
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        with(client) {
            addInterceptor(loggingInterceptor)
            addInterceptor(getBaseParameterInterceptor())
        }

        return client.build()
    }

    fun getBaseParameterInterceptor(): Interceptor {
        val baseParameterInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val addUrl: HttpUrl = originalRequest.url
                .newBuilder()
                .addQueryParameter("method", API.LOTO_METHOD)
                .build()

            val newRequest: Request = originalRequest.newBuilder()
                .url(addUrl)
                .method(originalRequest.method, originalRequest.body)
                .build()

            val response: Response = chain.proceed(newRequest)
            response
        }
        return baseParameterInterceptor
    }

    val lottoService: IRetrofit = lottoClient.create(IRetrofit::class.java)
}
