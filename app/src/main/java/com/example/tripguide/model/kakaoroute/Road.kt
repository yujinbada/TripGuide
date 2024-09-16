package com.example.tripguide.model.kakaoroute

data class Road(
    val distance: Int,
    val duration: Int,
    val name: String,
    val traffic_speed: Int,
    val traffic_state: Int,
    val vertexes: List<Double>
)