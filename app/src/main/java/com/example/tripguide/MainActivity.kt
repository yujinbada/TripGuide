package com.example.tripguide

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.example.tripguide.databinding.ActivityMainBinding
import com.example.tripguide.fragment.DepartRegionFragment
import com.example.tripguide.fragment.FirstFragment
import com.example.tripguide.fragment.MainFragment
import com.example.tripguide.fragment.dispositionfragment.*
import com.example.tripguide.utils.Constants


class MainActivity() : AppCompatActivity() {
    val TAG: String = "로그"

    private lateinit var binding: ActivityMainBinding

    private val dispositionFragment = DispositionFragment()
    private val firstFragment = FirstFragment()
    private val departRegionFragment = DepartRegionFragment()
    private val mainFragment = MainFragment()
    private val dispositionFragment2 = DispositionFragment2()
    private val dispositionFragment22 = DispositionFragment22()
    private val dispositionFragment3 = DispositionFragment3()
    private val dispositionFragment4 = DispositionFragment4()

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
                    .add(R.id.fragment_container_view, departRegionFragment, "depart")
                    .hide(dispositionFragment)
                    .addToBackStack("depart")
                    .commit()
            }

            3 -> {
                Log.d(TAG, "DepartRegionFragment -> DispositionFragment")
                transaction
                    .remove(departRegionFragment)
                    .show(dispositionFragment)
                    .commit()
                supportFragmentManager.popBackStack("depart", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            4 -> {
                Log.d(Constants.TAG, "FirstFragment -> MainFragment")
                transaction
                    .replace(R.id.fragment_container_view, mainFragment)
                    .commit()
            }
            5 -> {
                Log.d(TAG, "DispositionFragment -> DispositionFragment2")
                transaction
                    .add(R.id.fragment_container_view, dispositionFragment2)
                    .hide(dispositionFragment)
                    .addToBackStack(null)
                    .commit()
            }
            6 -> {
                Log.d(TAG, "DispositionFragment -> MainFragemnt")
                transaction
                    .remove(dispositionFragment)
                    .show(mainFragment)
                    .commit()
            }
            66 -> {
                Log.d(TAG, "DispositionFragment2 -> DispositionFragment")
                transaction
                    .remove(dispositionFragment2)
                    .show(dispositionFragment)
                    .commit()
            }
            7 -> {
                Log.d(TAG, "DispositionFragment2 -> DispositionFragment22")
                transaction
                    .add(R.id.fragment_container_view, dispositionFragment22)
                    .hide(dispositionFragment2)
                    .addToBackStack(null)
                    .commit()
            }
            8 -> {
                Log.d(TAG, "DispositionFragment2, DisposiotnFragment22 -> DispositionFragment3")
                transaction
                    .add(R.id.fragment_container_view, dispositionFragment3)
                    .hide(dispositionFragment2)
                    .hide(dispositionFragment22)
                    .addToBackStack(null)
                    .commit()
            }
            9 -> {
                Log.d(TAG, "DispositionFragment22 -> DispositionFragment2")
                transaction
                    .remove(dispositionFragment22)
                    .show(dispositionFragment2)
                    .commit()
            }
            10 -> {
                Log.d(TAG, "DispositionFragment3 -> DispositionFragment4")
                transaction
                    .add(R.id.fragment_container_view, dispositionFragment4)
                    .hide(dispositionFragment3)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}




