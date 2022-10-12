package com.example.tripguide.model

import android.os.Parcelable
import android.service.quicksettings.Tile
import kotlinx.parcelize.Parcelize
import java.io.Serializable

class MyModel(
    val firstregion: String? = null,
    val secondregion: String? = null
)

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
    val areaCode : String? = null,
    val siGunGuCode : String? = null
)

data class SelectItem (
    val firstimage: String?,
    val title: String?,
    val type: Int?)

data class RecommendItem (
    val recommendImage : String? = null,
    val recommendTitle : String? = null,
    val recommendcontentId : String? = null,
    var tourOverView : String? = null,
    var recommendmapX : String? = null,
    var recommendmapY : String? = null,
)
