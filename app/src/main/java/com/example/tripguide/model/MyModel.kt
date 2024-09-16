package com.example.tripguide.model

import java.time.LocalTime

data class MyModel(
    val firstregion: String? = null,
    val secondregion: String? = null
)

data class Station(
    val name: String?, // 장소명
    val road: String?, // 도로명 주소
    val address: String?, // 지번 주소
    val x: Double?, // 경도(Longitude)
    val y: Double?) // 위도(Latitude)

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
    var type: Int?,
    var mapX : String? = null,
    var mapY : String? = null,
    var liveTime: LocalTime? = null
    )

data class FinalItem (
    var selectItem: SelectItem?,
    var date : String?
)
data class GrandFinalItem(
    val finalItemList: ArrayList<FinalItem>,
    var date : String?
        )


data class RecommendItem (
    val recommendImage : String? = null,
    val recommendTitle : String? = null,
    val recommendcontentId : String? = null,
    var tourOverView : String? = null,
    var recommendmapX : String? = null,
    var recommendmapY : String? = null,
)



data class RouteResult (
    var key: String?,
    var duration : Int?
        )
