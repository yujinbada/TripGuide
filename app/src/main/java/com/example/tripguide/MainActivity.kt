/*
 An activity that defines a function for moving between fragments.
*/

package com.example.tripguide

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.pm.PackageInfo
import android.icu.number.Scale.none
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripguide.databinding.ActivityMainBinding
import com.example.tripguide.fragment.DepartRegionFragment
import com.example.tripguide.fragment.FirstFragment
import com.example.tripguide.fragment.MainFragment
import com.example.tripguide.fragment.SelectTourFragment
import com.example.tripguide.fragment.dispositionfragment.*
import com.example.tripguide.utils.Constants
import com.google.common.base.CharMatcher.none
import android.content.pm.PackageManager
import android.util.Base64
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tripguide.fragment.recommend.RecommendFragment1
import com.kakao.util.maps.helper.Utility
import kotlinx.coroutines.selects.select
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64.getEncoder


class MainActivity() : AppCompatActivity() {
    val TAG: String = "로그"

    private lateinit var binding: ActivityMainBinding

    private val mainFragment = MainFragment()
    private val dispositionFragment = DispositionFragment()
    private val firstFragment = FirstFragment()
    private val departRegionFragment = DepartRegionFragment()
    private val dispositionFragment2 = DispositionFragment2()
    private val dispositionFragment22 = DispositionFragment22()
    private val dispositionFragment3 = DispositionFragment3()
    private val dispositionFragment4 = DispositionFragment4()
    private val dispositionFragment5 = DispositionFragment5()
    private val dispositionFragment6 = DispositionFragment6()
    private val selectTourFragment = SelectTourFragment()

//    lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity - onCreate() called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
//
//        myViewModel.currentValue.observe(this, Observer {
//            Log.d(TAG, "MainActivity - myViewmodel - currentValue 라이브 데이터 값 변경 : $it")
//
//        })

        // setting the first fragment when the app is started
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, firstFragment)
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

        // set status bar to transparent
        fun Activity.setStatusBarTransparent() {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        var keyHash = Utility.getKeyHash(this)
        Log.d(TAG, "keyhash : $keyHash")
//    fun addFragment(a: Fragment, b: Fragment) {
//        Log.d(TAG, "$a -> $b")
//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragment_container_view, b)
//            .show(b)
//            .hide(a)
//            .addToBackStack(null)
//            .commit()
//    }
    }


        // supportFragmentManager function for fragment transaction
        fun changeFragment(index: Int?) {
            val transaction = supportFragmentManager.beginTransaction()

            when (index) {
                1 -> {
                    Log.d(TAG, "MainFragment -> DispositionFragment")
                    transaction
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
                        .hide(mainFragment)
                        .add(R.id.fragment_container_view, dispositionFragment)
                        .show(dispositionFragment)
                        .addToBackStack(null)
                        .commit()
                }
                2 -> {
                    Log.d(TAG, "DispositionFragment -> DepartRegionFragment")
                    transaction
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
                        .add(R.id.fragment_container_view, departRegionFragment, "depart")
                        .hide(dispositionFragment)
                        .disallowAddToBackStack()
                        .commit()
                }

                3 -> {
                    Log.d(TAG, "DepartRegionFragment -> DispositionFragment")
                    transaction
                        .setCustomAnimations(R.anim.none,
                            R.anim.horizon_exit,
                            R.anim.none,
                            R.anim.none)
                        .remove(departRegionFragment)
                        .show(dispositionFragment)
                        .disallowAddToBackStack()
                        .commit()
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
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
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
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
                        .add(R.id.fragment_container_view, dispositionFragment22)
                        .hide(dispositionFragment2)
                        .addToBackStack(null)
                        .commit()
                }
                8 -> {
                    Log.d(TAG, "DispositionFragment2, DisposiotnFragment22 -> DispositionFragment3")
                    transaction
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
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
                        .hide(dispositionFragment22)
                        .show(dispositionFragment2)
                        .commit()
                }
                10 -> {
                    Log.d(TAG, "DispositionFragment3 -> DispositionFragment4")
                    transaction
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
                        .add(R.id.fragment_container_view, dispositionFragment4)
                        .hide(dispositionFragment3)
                        .addToBackStack(null)
                        .commit()
                }
                11 -> {
                    Log.d(TAG, "DispositionFragment4 -> DispositionFragment3")
                    transaction
                        .remove(dispositionFragment4)
                        .hide(dispositionFragment4)
                        .show(dispositionFragment3)
                        .commit()
                }
                13 -> {
                    Log.d(TAG, "DispositionFragment3 -> DispositionFragment5")
                    transaction
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
                        .add(R.id.fragment_container_view, dispositionFragment5)
                        .hide(dispositionFragment3)
                        .addToBackStack(null)
                        .commit()

                }
                14 -> {
                    Log.d(TAG, "DispositionFragment3 -> DispositionFragment2")
                    transaction
                        .hide(dispositionFragment3)
                        .show(dispositionFragment2)
                        .commit()
                }
                15 -> {
                    Log.d(TAG, "DispositionFragment5 -> SelectTourFragment")
                    transaction
                        .add(R.id.fragment_container_view, selectTourFragment)
                        .show(selectTourFragment)
                        .hide(dispositionFragment5)
                        .addToBackStack(null)
                        .commit()
                }
                16 -> {
                    Log.d(TAG, "SelectTourFragment -> DispositionFragment5")
                    transaction
                        .remove(selectTourFragment)
                        .hide(selectTourFragment)
                        .show(dispositionFragment5)
                        .commit()
                }
                17 -> {
                    Log.d(TAG, "DispositionFragment5 -> DispositionFragment3")
                    transaction
                        .hide(dispositionFragment5)
                        .show(dispositionFragment3)
                        .commit()
                }
                18 -> {
                    Log.d(TAG, "DispositionFragment5 -> DispositionFragment6")
                    transaction
                        .setCustomAnimations(R.anim.horizon_enter,
                            R.anim.none,
                            R.anim.none,
                            R.anim.horizon_exit)
                        .add(R.id.fragment_container_view, dispositionFragment6)
                        .hide(dispositionFragment5)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
