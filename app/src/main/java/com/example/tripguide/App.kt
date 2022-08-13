package com.example.tripguide

import android.app.Application

data class TripGuide (var strName : String? = null,
                      var uid : String? = null,
                      var userId : String? = null,
                      var tripName : String? = null,
                      var timestamp : Long? = null,
                      var departure : String? = null,
                      var arrival : String? = null,
                      var date : String? = null,
                      var with : String? = null,
                      var style : String? = null,
                      var arrival_how : String? = null,
                      var departure_how : String? = null,
                      var must_sights : String? = null,
                      var must_restaurant : String? = null,
                      var must_hotel : String? = null) {
}

class App : Application() {
    companion object{
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
