package com.example.tripguide

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.tripguide.utils.SEARCH_REGION
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity() {

    val TAG: String = "로그"
    lateinit var navController: NavController

    private var currentSarchType: SEARCH_REGION = SEARCH_REGION.REGION

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity - onCreate() called")
        navController = nav_host_fragment.findNavController()
        fun Activity.setStatusBarTransparent() {
            window.setFlags( // 상태바 투명화 함수
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

    }
}

