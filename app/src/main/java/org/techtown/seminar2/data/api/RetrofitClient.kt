package org.techtown.seminar2.data.api

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import org.techtown.seminar2.util.*
import org.techtown.seminar2.util.Constants.TAG
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val retrofitClient: Retrofit = Retrofit.Builder()
        .baseUrl(API.BASE_URL)
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

        // client에 로깅인터셉터, 기본 파라메터 추가
        with(client) {
            addInterceptor(loggingInterceptor)
            addInterceptor(getBaseParameterInterceptor())
            connectTimeout(10, TimeUnit.SECONDS) // 연결 타임아웃
            readTimeout(10, TimeUnit.SECONDS) // 읽기 타임아웃
            writeTimeout(10, TimeUnit.SECONDS) // 쓰기 타임아웃
            retryOnConnectionFailure(true) // 실패시 다시 시도
        }

        return client.build()
    }

    // 2) 기본 파라메터 인터셉터 설정
    fun getBaseParameterInterceptor(): Interceptor {
        val baseParameterInterceptor: Interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val originalRequest = chain.request()

                // 쿼리 파라메터 추가
                val addUrl: HttpUrl = originalRequest.url
                    .newBuilder()
                    .addQueryParameter("client_id", API.CLIENT_ID)
                    .build()

                val newRequest: Request = originalRequest.newBuilder()
                    .url(addUrl)
                    .method(originalRequest.method, originalRequest.body)
                    .build()

                val response: Response = chain.proceed(newRequest)
                if (response.code != 200) {
                    Handler(Looper.getMainLooper()).post {
                        App.instance.showToast("${response.code}에러입니다.")
                    }
                }
                return response
            }
        }
        return baseParameterInterceptor
    }

    val photoService: IRetrofit = retrofitClient.create(IRetrofit::class.java)
}
