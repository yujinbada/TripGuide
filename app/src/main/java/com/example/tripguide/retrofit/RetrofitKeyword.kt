package com.example.tripguide.retrofit

import com.example.tripguide.model.kakao.kakaokeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitKeyword {
    @GET("/v2/local/search/keyword.json")
    fun getKakaoKeyword(
        @Header("Authorization") key: String,
        @Query("query") address: String
    ): Call<kakaokeyword>
}
