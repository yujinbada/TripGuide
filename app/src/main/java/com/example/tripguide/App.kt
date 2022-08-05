package com.example.tripguide

import android.app.Application

data class TripGuide (var strName : String? = null,
                      var uid : String? = null,
                      var userId : String? = null,
                      var tripName : String? = null,
                      var timestamp : Long? = null,
                      var departure : String? = null,
                      var arrival : String? = null,
                      var date : String? = null) {
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
