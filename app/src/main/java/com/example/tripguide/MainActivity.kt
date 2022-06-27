package com.example.tripguide

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.tripguide.databinding.ActivityMainBinding
import com.example.tripguide.fragment.DepartRegionFragment
import com.example.tripguide.fragment.DispositionFragment
import com.example.tripguide.fragment.FirstFragment
import com.example.tripguide.fragment.MainFragment
import com.example.tripguide.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity() {
    val TAG: String = "로그"

    private lateinit var binding: ActivityMainBinding

    private val dispositionFragment = DispositionFragment()
    private val firstFragment = FirstFragment()
    private val departRegionFragment = DepartRegionFragment()
    private val mainFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, firstFragment)
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

        Log.d(TAG, "MainActivity - onCreate() called")
        fun Activity.setStatusBarTransparent() {
            window.setFlags( // 상태바 투명화 함수
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

    }

    fun changeFragment(index: Int?) {
        val transaction = supportFragmentManager.beginTransaction()
        when(index) {
            1 -> {
                Log.d(TAG, "MainFragment -> DispositionFragment")
                transaction
                    .add(R.id.fragment_container_view, dispositionFragment)
                    .show(dispositionFragment)
                    .hide(mainFragment)
                    .addToBackStack(null)
                    .commit()
            }
            2 -> {
                Log.d(TAG, "depart: DispositionFragment -> DepartRegionFragment")
                transaction
                    .add(R.id.fragment_container_view, departRegionFragment)
                    .show(departRegionFragment)
                    .hide(dispositionFragment)
                    .addToBackStack(null)
                    .commit()
            }

            3 -> {
                Log.d(TAG, "DepartRegionFragment -> DispositionFragment")
                transaction
                    .remove(departRegionFragment)
                    .show(dispositionFragment)
                    .addToBackStack(null)
                    .commit()
            }

            4 -> {
                Log.d(Constants.TAG, "FirstFragment -> MainFragment")
                transaction
                    .replace(R.id.fragment_container_view, mainFragment)
                    .commit()
            }
        }
    }

}




