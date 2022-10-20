package com.example.tripguide.model.kakaoroute

data class Route(
    val key: String,
    val result_code: Int,
    val result_msg: String,
    val summary: Summary
)