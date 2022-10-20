package com.example.tripguide.retrofit

import com.example.tripguide.model.Destination
import retrofit2.Call
import com.example.tripguide.model.kakao.KakaoData
import com.example.tripguide.model.Origin
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Objects

interface RetrofitRoute {
    @POST("v1/destinations/directions")
    fun getKakaoRoute(
        @Header("Authorization") key: String,
        @Query("origin") origin: Origin,
        @Query("destinations") destinations : ArrayList<Destination>,
        @Query("radius") radius : Int,
        @Query("priority") priority : String
    ) : Call<KakaoData>
}