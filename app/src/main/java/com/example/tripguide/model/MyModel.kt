package com.example.tripguide.model

import android.os.Parcelable
import android.service.quicksettings.Tile
import kotlinx.parcelize.Parcelize

class MyModel(
    val firstregion: String? = null,
    val secondregion: String? = null)

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
    val readcount : String? = null)

@Parcelize
data class SelectItem (
    val firstimage: String?,
    val title: String? ) : Parcelable
{
    override fun toString(): String {
        return "firstimage:$firstimage, title:$title"
    }
}
