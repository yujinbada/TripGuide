package com.example.tripguide.model.kakaoroute

data class Summary(
    val bound: Bound,
    val destination: Destination,
    val distance: Int,
    val duration: Int,
    val fare: Fare,
    val origin: Origin,
    val priority: String,
    val waypoints: List<Any>
)