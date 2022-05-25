package com.example.tripguide

import android.util.Log

class Recommend_model (var name: String? = null, var cityimg: String? = null){
    val TAG: String = "로그"

    init {
        Log.d(TAG, "Recommend_model - init() called")
    }
}