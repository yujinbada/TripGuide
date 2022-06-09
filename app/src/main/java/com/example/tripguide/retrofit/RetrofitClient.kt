package com.example.tripguide.retrofit

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.tripguide.App
import com.example.tripguide.utils.API
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.isJsonArray
import com.example.tripguide.utils.isJsonobject
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private const val BASE_URL = "https://openapi.naver.com/v1/"
    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit? {
        Log.d(TAG, "RetrofitCliet - getClient() called")

        //okhttp 인스턴스 생성
        val client = OkHttpClient.Builder()
        //로그를 찍기위해 로깅 인터셉터 설정
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(TAG, "RetrofitCliet - log() called / message: $message")

            when {
                message.isJsonobject() ->
                    Log.d(TAG, JSONObject(message).toString(4))
                message.isJsonArray() ->
                    Log.d(TAG, JSONArray(message).toString(4))
                else -> {
                    try {
                        Log.d(TAG, JSONObject(message).toString(4))
                    } catch (e: Exception) {
                        Log.d(TAG, message)
                    }
                }
            }
        }
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        //위에서 설정한 로깅 인터셉터를 okttp 클라이언트에 추가
        client.addInterceptor(loggingInterceptor)

        //기본 파라미터 인터셉터 설정
        val baseParamterInterceptor : Interceptor = (object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "RetrofitCliet - intercept() called")
                //오리지날 리퀘스트
                val originalRequest = chain.request()
                //쿼리 파라메터 추가하기
                val addUrl = originalRequest.url.newBuilder().addQueryParameter("client_id", API.CLIENT_ID).build()

                val finalRequest = originalRequest.newBuilder()
                    .url(addUrl)
                    .method(originalRequest.method, originalRequest.body)
                    .build()

                return chain.proceed(finalRequest)
                val response = chain.proceed(finalRequest)

                if(response.code != 200) {
                    Handler(Looper.getMainLooper()).post{
                        Toast.makeText(App.instance, "${response.code} 에러 입니다", Toast.LENGTH_SHORT).show()
                    }
                }

                return response
            }

        })
        //위에서 설정한 기본파라미터 인터셉터를 okttp 클라이언트에 추가
        client.addInterceptor(baseParamterInterceptor)

        //커넷션 타임아웃
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.readTimeout(10, TimeUnit.SECONDS)
        client.writeTimeout(10, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true)


        val gson = GsonBuilder()
                .setLenient()
                .create()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }
        return retrofit
    }
}