package com.example.tripguide.kakao

data class Document(
    var place_name: String, // 장소명, 업체명
    val address: Address,
    val address_name: String,
    var road_address_name: String,
    val address_type: String,
    val road_address: Any,
    val x: String,
    val y: String
)