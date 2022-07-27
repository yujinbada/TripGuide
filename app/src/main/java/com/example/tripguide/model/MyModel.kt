package com.example.tripguide.model

class MyModel(val firstregion: String,
              val secondregion: String)

data class FirebaseClass( var name : String? = null,
                          var email : String? = null,
                          var photoUrl : String? = null,
                          var emailVerified : String? = null,
                          var uid : String? = null) {

}
data class Tour ( val firstimage : String,
                  val title : String,
                  val addr1 : String,
                  val readcount : String)
