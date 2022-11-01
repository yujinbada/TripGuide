package com.example.tripguide.model.kakaoroute

data class Route(
    val result_code: Int,
    val result_msg: String,
    val sections: List<Section>,
    val summary: Summary
)