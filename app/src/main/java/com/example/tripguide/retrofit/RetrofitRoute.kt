package com.example.tripguide.retrofit

import com.example.tripguide.model.kakaoroute.Destination
import com.example.tripguide.model.kakaoroute.KakaoRoute
import com.example.tripguide.model.kakaoroute.Origin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface RetrofitRoute {
    @GET("v1/directions")
    fun getKakaoRoute(
        @Header("Authorization") key: String,
        @Query("origin") origin: Origin,
        @Query("destination") destination: Destination,
        @Query("priority") priority : String,
        @Query("summary") summary : Boolean
    ) : Call<KakaoRoute>
}