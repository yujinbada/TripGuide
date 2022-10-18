package com.example.tripguide.model

import android.os.Parcelable
import android.service.quicksettings.Tile
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class MyModel(
    val firstregion: String? = null,
    val secondregion: String? = null
)

data class Station(
    val name: String, // 장소명
    val road: String, // 도로명 주소
    val address: String, // 지번 주소
    val x: Double, // 경도(Longitude)
    val y: Double) // 위도(Latitude)

data class FirebaseClass(
    var name : String? = null,
    var email : String? = null,
    var photoUrl : String? = null,
    var emailVerified : String? = null,
    var uid : String? = null) {

}

data class Tour (
    val firstimage : String? = null,
    val title : String? = null,
    val addr1 : String? = null,
    var mapX : String? = null,
    var mapY : String? = null,
)

data class SelectItem (
    val firstimage: String?,
    val title: String?,
    val type: Int?,
    var mapX : String? = null,
    var mapY : String? = null)

data class RecommendItem (
    val recommendImage : String? = null,
    val recommendTitle : String? = null,
    val recommendcontentId : String? = null,
    var tourOverView : String? = null,
    var recommendmapX : String? = null,
    var recommendmapY : String? = null,
)

data class TourRoute (
    val key : String?,
    val type :String,
    var mapX : String? = null,
    var mapY : String? = null
        )
