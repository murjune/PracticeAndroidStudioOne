# Practice-Android-Studio
Sopt 세미나에서 배운 내용 복습 및 새로운 기능을 연습하기 위한 레포입니다.
- 서버 통신 연습  
- 리사이클러뷰 복습 및 적용
- 다양한 머터리얼 디자인 연습하기
- 뷰페이저, 바텀네비, 네비게이션 컴포넌트 복습
- 코루틴 실습
- 뷰모델 실습
- 뷰모델과 코루틴을 활용하여 서버연결을 마무리하는 것이 제 목표입니다 ㅎ ㅎ

# Chapter 1: 서버 통신 연습!!
- [서버 연습 사이트](https://unsplash.com/documentation#search-photos)에서 실습하도록 하겠슴니다.
## Intercepter
> 클라와 서버 간에 Retrofit or OkHttp를 사용하여 통신을 하는데 
> 인터셉터를 추가로 사용하면 클라에서 서버로 데이터 전송 및 수신받을때 `intercepter`라는 녀석이 중간에 개입해서  
> 기본 매개변수를 추가, 로그확인, 사용자 위치 추가 등 다양한 처리를 해줄 수 있다.
### 1. LoggingIntercepter
```kotlin
// RetrofitClient.Kt
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
private val retrofitClient: Retrofit = Retrofit.Builder()
    .baseUrl(API.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    // (추가) 6. 레트로핏 클라이언트 추가
    .client(getHttpClient())
    .build()
```
기존에는 다음과 같이 로그가 찍혔지만
```
2022-06-18 22:01:36.782 19134-19134/org.techtown.seminar2 D/로그: SearchActivity - onResponse() called/ response : null
2022-06-18 22:01:36.793 19134-19134/org.techtown.seminar2 D/로그: api 호출 error: null
```
로깅 인터셉터를 OkHTTP객체에 추가해준 후, OkHttp객체를 레트로핏객체에 추가해주었더니 다음과 같이 로그가 찍힌다!!  
```
2022-06-18 22:01:36.778 19134-19175/org.techtown.seminar2 D/로그: RetrofitClient - log() called/ message: <-- 401 https://api.unsplash.com/search/photos?query=cat (487ms)
2022-06-18 22:01:36.778 19134-19175/org.techtown.seminar2 D/로그: <-- 401 https://api.unsplash.com/search/photos?query=cat (487ms)
.. 생략
2022-06-18 22:01:36.781 19134-19175/org.techtown.seminar2 D/로그: RetrofitClient - log() called/ message: x-timer: S1655557219.784767,VS0,VE233
2022-06-18 22:01:36.781 19134-19175/org.techtown.seminar2 D/로그: x-timer: S1655557219.784767,VS0,VE233
2022-06-18 22:01:36.781 19134-19175/org.techtown.seminar2 D/로그: RetrofitClient - log() called/ message: vary: Accept-Encoding, Origin,Authorization,Accept-Language,client-geo-region,Accept
2022-06-18 22:01:36.781 19134-19175/org.techtown.seminar2 D/로그: vary: Accept-Encoding, Origin,Authorization,Accept-Language,client-geo-region,Accept
```
포스트맨에서 `Headers`를 찍어서 나온 값과 같다 :D  

<img width = 700 src="https://user-images.githubusercontent.com/87055456/174439321-df93e466-523f-4551-8984-c5b48f5c08f9.png">   

logging인터셉터 level을 다음과 같이 `Body`로 설정할 수도 있다  
```kotlin
// 3. loggingInterceptor의 level설정
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
```
- 실행결과
```
2022-06-18 22:19:46.122 19394-19437/org.techtown.seminar2 D/로그: RetrofitClient - log() called/ message: {"errors":["OAuth error: The access token is invalid"]}
2022-06-18 22:19:46.122 19394-19437/org.techtown.seminar2 D/로그: {
        "errors": [
            "OAuth error: The access token is invalid"
        ]
    }
```  

<img width = 700 src="https://user-images.githubusercontent.com/87055456/174439522-7d6fe1ac-befd-431e-8752-6b2dc77f69d9.png">  

### 2. 기본 파라미터 추가
솔직히 완벽히 이해하지 못했다.. 이 부분을 좀 더 추가로 공부해야할듯..
```kotlin
// 2) 기본 파라메터 인터셉터 설정
    fun getBaseParameterInterceptor(): Interceptor {
        val baseParameterInterceptor: Interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val originalRequest = chain.request()

                // 쿼리 파라메터 추가
                val addUrl: HttpUrl = originalRequest.url
                    .newBuilder()
                        // 쿼리 추가
                    .addQueryParameter("client_id", API.CLIENT_ID)
                    .build()

                val newRequest: Request = originalRequest.newBuilder()
                    .url(addUrl)
                    .method(originalRequest.method, originalRequest.body)
                    .build()

                return chain.proceed(newRequest)
            }
        }
        return baseParameterInterceptor
    }
// 클라이언트에 인터셉터 추가
client.addInterceptor(getBaseParameterInterceptor())
```
- 실행결과  

<img width = 700 src="https://user-images.githubusercontent.com/87055456/174442783-ee448654-18b1-41cd-b1c1-08417cb6b534.png">  

```
2022-06-18 23:03:51.669 19843-19883/org.techtown.seminar2 D/로그: RetrofitClient - log() called/ message: {"total":10000,"total_pages":1000,"results":[{"id":"gKXKBY-C-Dk","created_at":"2018-01-02T05:20:47-05:00","updated_at":"2022-06-18T03:02:34-04:00","promoted_at":null,"width":5026,"height":3458,"color":"#598c73","blur_hash":"LDCtq6Me0_kp3mof%MofUwkp,cRP","description":"Gipsy the Cat was sitting on a bookshelf one afternoon and just stared right at me, kinda saying: “Will you take a picture already?”","alt_description":"black and white cat lying on brown bamboo chair inside room","urls":{"raw":"https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixid=MnwzMzg2MDd8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjU1NTYxMzYz\u0026ixlib=rb-1.2.1","full":"https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?crop=entropy\u0026cs=tinysrgb\u0026fm=jpg\u0026ixid=MnwzMzg2MDd8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjU1NTYxMzYz\u0026ixlib=rb-1.2.1\u0026q=80","regular":"https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwzMzg2MDd8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjU1NTYxMzYz\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=1080","small":"https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwzMzg2MDd8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjU1NTYxMzYz\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=400","thumb":"https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwzMzg2MDd8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjU1NTYxMzYz\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=200","small_s3":"https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1514888286974-6c03e2ca1dba"},"links":{"self":"https://api.unsplash.com/photos/gKXKBY-C-Dk","html":"https://unsplash.com/photos/gKXKBY-C-Dk","download":"https://unsplash.com/photos/gKXKBY-C-Dk/download?ixid=MnwzMzg2MDd8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjU1NTYxMzYz","download_location":"https://api.unsplash.com/photos/gKXKBY-C-Dk/download?ixid=MnwzMzg2MDd8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjU1NTYxMzYz"},"categories":[],"likes":1019,"liked_by_user":false,"current_user_collections":[],"sponsorship":null,"topic_submissions":{},"user":{"id":"wBu1hC4QlL0","updated_at":"2022-06-17T02:02:10-04:00","username":"madhatterzone","name":"Manja Vitolic","first_name":"Manja","last_name":"Vitolic","twitter_username":null,"portfolio_url":"https://www.instagram.com/makawee_photography/?hl=en","bio":"https://www.instagram.com/makawee_photography/","location":"Wiesbaden, Germany","links":{"self":"https://api.unsplash.com/users/madhatterzone","html":"https://unsplash.com/@madhatterzone","photos":"https://api.unsplash.com/users/madhatterzone/photos","likes":"https://api.unsplash.com/users/madhatterzone/likes","portfolio":"https://api.unsplash.com/users/madhatterzone/portfolio","following":"https://api.unsplash.com/users/madhatterzone/following","followers":"https://api.unsplash.com/users/madhatterzone/followers"},"profile_image":{"small":"https://images.unsplash.com/profile-fb-1514888261-0e72294039e0.jpg?ixlib=rb-1.2.1\u0026crop=faces\u0026fit=crop\u0026w=32\u0026h=32","medium":"https://images.unsplash.com/profile-fb-1514888261-0e72294039e0.jpg?ixlib=rb-1.2.1\u0026crop=faces\u0026fit=crop\u0026w=64\u0026h=64","large":"https://images.unsplash.com/profile-fb-1514888261-0e72294039e0.jpg?ixlib=rb-1.2.1\u0026crop=faces\u0026fit=crop\u0026w=128\u0026h=128"},"instagram_username":"makawee_photography","total_collections":0,"total_likes":10,"total_photos":65,"accepted_tos":true,"for_hire":true,"social":{"instagram_username":"makawee_photography","portfolio_url":"https://www.instagram.com/makawee_photography/?hl=en","twitter_username":null,"paypal_email":null}},"tags":[{"type":"landing_page","title":"cat","source":{"ancestry":{"type":{"slug":"images","pretty_slug":"Images"},"category":{"slug":"animals","pretty_slug":"Animals"},"subcategory":{"slug":"cat","pretty_slug":"Cat"}},"title":"Cat images \u0026 pictures","subtitle":"Download free cat image
2022-06-18 23:03:51.706 19843-19883/org.techtown.seminar2 D/로그: {
        "total": 10000,
        "total_pages": 1000,
        "results": [
            {
                "id": "gKXKBY-C-Dk",
                "created_at": "2018-01-02T05:20:47-05:00",
                "updated_at": "2022-06-18T03:02:34-04:00",
                "promoted_at": null,
                "width": 5026,
                "height": 3458,
                "color": "#598c73",
                "blur_hash": "LDCtq6Me0_kp3mof%MofUwkp,cRP",
                "description": "Gipsy the Cat was sitting on a bookshelf one afternoon and just stared right at me, kinda saying: “Will you take a picture already?”",
                "alt_description": "black and white cat lying on brown bamboo chair inside room",
                "urls": {
                .. 생략
```
- 포스트맨과 비교

### 3. TimeOut
- 연결할 수 없는 경우 시간 초과를 사용하여 호출에 실패한다.
- 네트워크 오류는 클라이언트 연결 문제, 서버 가용성 문제 또는 그 사이의 모든 문제로 발생할 수 있다.
- Okhttp는 연결, 읽기 및 쓰기 제한 시간을 지원한다.  
- 기본값: 10초
```kotlin
with(client ){
    connectTimeout(10, TimeUnit.SECONDS) // 연결 타임아웃
    readTimeout(10, TimeUnit.SECONDS)   // 읽기 타임아웃
    writeTimeout(10, TimeUnit.SECONDS)  // 쓰기 타임아웃
    retryOnConnectionFailure(true) // 실패시 다시 시도
} 
```
## 서버통신 오류 해결
```
onFailure() called/ t: javax.net.ssl.SSLHandshakeException: Chain validation failed
```
위와 같은 오류가 발생했다..  
처음보는 오류라 찾아봤더니, 에뮬레이터의 시간과 서버통신되는 시점이 비슷하지 않으면 오류가 발생한다고 한다..
- [참고문헌](https://geekcarrot.net/ko/android-%EC%8A%A4%EB%A7%88%ED%8A%B8%ED%8F%B0%EC%97%90%EC%84%9C-https-ssl-tsl-%EC%97%B0%EA%B2%B0-%EC%98%A4%EB%A5%98-%EC%88%98%EC%A0%95)
```
SSL/TSL 인증서는 브라우저 시스템과 웹 서버의 시계가 거의 같은 시간으로 설정되어 있지 않은 경우
 연결이 잘못된 것으로 간주
```

# Chapter 2: 로또 번호 생성기(코루틴 예제)
- [심화 스터디 링크](https://www.notion.so/q-bit/Coroutine-ae151ba9ec7d4118938c74a5652af618)  
- 수빈님의 과제!!  
- 1단계) 세미나에서 배운대로 CallBack + Interceptor 공부한 거로 구현(복습)  
- 2단계) 코루틴 적용하기
- 3단계) ViewModel사용해서 역할분리하자
## 1단계
```kotlin
class LottoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLottoBinding
    private var lottoNums = mutableListOf<Int>()
    private lateinit var myLottoNum: MutableList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLottoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNetWork()
        onClickButton()
    }

    private fun onClickButton() {
        binding.btnResult.setOnClickListener {
            if (lottoNums.isNotEmpty()) {
                createMyLottoNum()
                val result = getLottoResult()
                changeMyLottoNum()
                binding.tvWinning.text = "$lottoNums \n $result"
            } else {
                showToast("아직 서버에서 로또번호 못가져옴 ㅜ")
            }
        }
    }
    private fun changeMyLottoNum() {
        with(binding) {
            tvNum1.text = myLottoNum[0].toString()
            tvNum2.text = myLottoNum[1].toString()
            tvNum3.text = myLottoNum[2].toString()
            tvNum4.text = myLottoNum[3].toString()
            tvNum5.text = myLottoNum[4].toString()
            tvNum6.text = myLottoNum[5].toString()
        }
    }
    private fun createMyLottoNum() {
        Log.d(TAG, "LottoActivity - createLottoNum() called")
        val nums: MutableList<Int> = IntArray(45) { it + 1 }.toMutableList()
        nums.shuffle()

        myLottoNum = nums.slice(0..5).toMutableList()
    }

    private fun getLottoResult(): String {
        Log.d(TAG, "LottoActivity - getLottoResult() called")
        Log.d(TAG, "LottoActivity - getLottoResult() - 내 로또번호: $myLottoNum")
        Log.d(TAG, "LottoActivity - getLottoResult() - 로또번호 : $lottoNums")
        var cnt = 0
        for (i in 0..5) {
            if (myLottoNum[i].equals(lottoNums[i])) {
                cnt++
            }
        }
        return when (cnt) {
            6 -> "1등"
            5 -> "2등"
            4 -> "3등"
            3 -> "4등"
            2 -> "5등"
            1 -> "6등"
            else -> "아무것도 없쥬?"
        }
    }

    private fun initNetWork() {
        Log.d(TAG, "LottoActivity - initNetWork() called")
        val call: Call<ResponseLottoNum> =
            LottoClient.lottoService.responseLottoInfo(ROUND)
        call.enqueue(object : Callback<ResponseLottoNum> {
            override fun onResponse(
                call: Call<ResponseLottoNum>,
                response: Response<ResponseLottoNum>
            ) {
                Log.d(TAG, "LottoActivity - onResponse() - ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.returnValue == SUCCESS) {
                            lottoNums.add(it.lottoNum_one)
                            lottoNums.add(it.lottoNum_two)
                            lottoNums.add(it.lottoNum_three)
                            lottoNums.add(it.lottoNum_four)
                            lottoNums.add(it.lottoNum_five)
                            lottoNums.add(it.lottoNum_six)
                            Log.d(TAG, "LottoActivity - onResponse() called : network 연결성공")
                        } else {
                            showToast("${it.returnValue} Error입니다.")
                            Log.d(
                                TAG,
                                "LottoActivity - onResponse() called :${it.returnValue} Error입니다."
                            )
                        }
                    }
                } else {
                    showToast("연결 오류 뜸")
                    Log.d(TAG, "${response.code()} error입니다.")
                }
            }

            override fun onFailure(call: Call<ResponseLottoNum>, t: Throwable) {
                Log.d(TAG, "response Code error - NetWork 연결 실패")
                showToast("NetWork 연결 실패")
            }
        })
    }

    companion object {
        const val ROUND = "10"
        const val SUCCESS = "success"
    }
}

```
## 오류
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLottoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNetWork() <-- 네트워크 연결하는 코드
        createLottoNum() <-- 나의 로또번호 생성하는 코드
        getLottoResult() <-- 로또 번호 비교해서 당첨인지 아닌지 확인하는 코드
    }
```
그냥 세미나에서 배운것과 Chapter1에서 공부한 내용을 복습하자~~라는 식으로 공부한건디..  
실행하자마자 앱이 터져버졌다..
오류내용을 보니까, 다음과 같이 `java.lang.IndexOutOfBoundsException`가 발생했다.

<img width="700" src="https://user-images.githubusercontent.com/87055456/174481505-375abb65-27db-439c-8142-c8e72412ffef.png">  

- 로그를 찍어보니 진짜 그렇넹..
```
D/로그: LottoActivity - getLottoResult() - 내 로또번호: [30, 16, 31, 26, 35, 24]
D/로그: LottoActivity - getLottoResult() - 로또번호 : []
```
나는 서버에서 json객체를 받아올 때, 문제가 생긴다고 생각하고 개애~~삽질을 했는데, 로그를 찍어보니 또 그건 아니였다.  
```
D/로그: RetrofitClient - log() called/ message: --> GET https://dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=10
```
- 그래서 오류난 이유는??
Callback에서 비동기적으로 로또번호를 lottoNums 리스트에 담아주기 전에  Main쓰레드에서 로또번호와  
나의 로또번호를 비교하려했기 때문에 예외가 발생한 것이였다!!
> 라면집에서 아직 라면이 나오지도 않았는데, 라면을 먹으려한 것과 같은 행위..  

이론적으로는 알고 있었지만, 직접 개발을 하면서 이런 일을 처음 겪기 때문에 상당히 당황스럽지만 뭔가 뿌듯?하다 ㅋㅋㅋ  

## 2단계: 코루틴 적용
- network처리해주는 부분 I/O Dispatcher로 설정  
- 
```kotlin
package org.techtown.seminar2.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.techtown.seminar2.data.api.LottoClient
import org.techtown.seminar2.databinding.ActivityLottoBinding
import org.techtown.seminar2.util.Constants.TAG
import org.techtown.seminar2.util.showToast
import java.util.*

class LottoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLottoBinding
    private val job = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLottoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickButton()

    }

    private fun onClickButton() {
        binding.btnResult.setOnClickListener {
            val myLottoNum = createMyLottoNum()
            changeMyLottoNum(myLottoNum)
            CoroutineScope(Dispatchers.IO + job).launch {
                val winningLottoNums = async { getWinningLottoNums() }
                val rank = getLottoResult(myLottoNum, winningLottoNums.await())
                withContext(Dispatchers.Main) {
                    binding.tvWinning.text = "${winningLottoNums.await()} : $rank"
                }
            }
        }
    }

    private fun changeMyLottoNum(myLottoNum: MutableList<Int>) {
        with(binding) {
            tvNum1.text = myLottoNum[0].toString()
            tvNum2.text = myLottoNum[1].toString()
            tvNum3.text = myLottoNum[2].toString()
            tvNum4.text = myLottoNum[3].toString()
            tvNum5.text = myLottoNum[4].toString()
            tvNum6.text = myLottoNum[5].toString()
        }
    }

    private fun createMyLottoNum(): MutableList<Int> {
        Log.d(TAG, "LottoActivity - createLottoNum() called")
        val nums: MutableList<Int> = IntArray(45) { it + 1 }.toMutableList()
        nums.shuffle()

        return nums.slice(0..5).toMutableList()
    }

    private suspend fun getLottoResult(
        lottoNums: MutableList<Int>?,
        myLottoNums: MutableList<Int>
    ): String {
        if (lottoNums == null) {
            return "네트워크 오류발생"
        }
        var cnt = 0
        for (i in 0..5) {
            if (myLottoNums[i].equals(lottoNums[i])) {
                cnt++
            }
        }
        return when (cnt) {
            6 -> "1등"
            5 -> "2등"
            4 -> "3등"
            3 -> "4등"
            2 -> "5등"
            1 -> "6등"
            else -> "아무것도 없쥬?"
        }
    }

    private suspend fun getWinningLottoNums(): MutableList<Int> {
        var WinlottoNums = mutableListOf<Int>()
        val response = LottoClient.lottoService.responseLottoInfo(ROUND)
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.returnValue == SUCCESS) {
                    WinlottoNums.add(it.lottoNum_one)
                    WinlottoNums.add(it.lottoNum_two)
                    WinlottoNums.add(it.lottoNum_three)
                    WinlottoNums.add(it.lottoNum_four)
                    WinlottoNums.add(it.lottoNum_five)
                    WinlottoNums.add(it.lottoNum_six)
                }
            }
            return WinlottoNums
        }
        return mutableListOf()
    }
    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    companion object {
        const val ROUND = "10"
        const val SUCCESS = "success"
    }
}
```
- 실행화면
<img width="600" src="https://user-images.githubusercontent.com/87055456/174488344-046db977-31fa-4a23-8d86-daae8c7b6e88.gif">