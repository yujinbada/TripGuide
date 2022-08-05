package com.example.tripguide.model

class MyModel(val firstregion: String? = null,
              val secondregion: String? = null)

data class FirebaseClass( var name : String? = null,
                          var email : String? = null,
                          var photoUrl : String? = null,
                          var emailVerified : String? = null,
                          var uid : String? = null) {

}
data class Tour ( val firstimage : String? = null,
                  val title : String? = null,
                  val addr1 : String? = null,
                  val readcount : String? = null)
