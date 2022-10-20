package com.example.tripguide.model.kakao

data class MetaX(
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: SameName,
    val total_count: Int
)