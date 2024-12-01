package com.example.kong_android

import retrofit2.Call
import com.example.kong_android.auth.LoginRequest
import com.example.kong_android.auth.LoginResponse
import com.example.kong_android.auth.SignupRequest
import com.example.kong_android.auth.SignupResponse
import com.example.kong_android.home.AnalysisResponse
import com.example.kong_android.home.HomeResponse
import com.example.kong_android.plan.PlanApiResponse
import com.example.kong_android.record.GetHistoryResponse
import com.example.kong_android.record.RecordRequest
import com.example.kong_android.record.RecordResponse
import com.google.gson.GsonBuilder
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitService {
    // 로그인
    @POST("/member/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // 회원 가입
    @POST("member/register")
    fun registerUser(@Body request: SignupRequest): Call<SignupResponse>

    // 금액 리스트
    @GET("/home")
    fun fetchHomeData(@Header("Authorization") token: String): Call<HomeResponse>

    // 카테고리 분석
    @GET("/history/analysis")
    fun fetchAnalysisData(@Header("Authorization") token: String): Call<AnalysisResponse>

    // 기록 조회
    @GET("/history")
    fun fetchHistoryData(@Header("Authorization") token: String): Call<List<GetHistoryResponse>>

    // 기록 등록
    @POST("/history")
    fun addRecord(
        @Header("Authorization") token: String,
        @Body request: RecordRequest
    ): Call<RecordResponse>

    // 계획 조회
    @GET("/plan")
    fun fetchPlanData(@Header("Authorization") token: String): Call<PlanApiResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://89.116.32.94:9090/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cookieJar = object : CookieJar {
        private val cookieStore: MutableMap<String, MutableList<Cookie>> = mutableMapOf()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies.toMutableList()
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: mutableListOf()
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: RetrofitService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(RetrofitService::class.java)
    }
}