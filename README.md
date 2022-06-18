# Practice-Android-Studio
Sopt 세미나에서 배운 내용 복습 및 새로운 기능을 연습하기 위한 레포입니다.
- 서버 통신 연습  
- 리사이클러뷰 복습 및 적용
- 다양한 머터리얼 디자인 연습하기
- 뷰페이저, 바텀네비, 네비게이션 컴포넌트 복습
- 코루틴 실습
- 뷰모델 실습
- 뷰모델 과 코루틴을 활용하여 서버연결을 마무리하는 것이 제 목표입니다 ㅎ ㅎ
# 서버 통신

## Chapter 2
- [서버 연습 사이트](https://unsplash.com/documentation#search-photos)에서 실습하도록 하겠슴니다.
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



