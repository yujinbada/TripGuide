package com.example.tripguide.model

import android.os.Parcelable
import android.service.quicksettings.Tile
import kotlinx.parcelize.Parcelize

class MyModel(
    val firstregion: String? = null,
    val secondregion: String? = null,
    val x: String? = null,
    val y: String? = null
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
    val addr1 : String? = null
)

data class SelectItem (
    val firstimage: String?,
    val title: String?,
    val type: Int?)

data class RecommendItem (
    val recommendImage : String? = null,
    val recommendTitle : String? = null,
    val tourOverview : String? = null
)

data class RecommendTourDetail (
    val tourOverview : String? = null
)

