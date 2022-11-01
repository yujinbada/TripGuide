/*
 An activity that defines a function for moving between fragments.
*/

package com.example.tripguide

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.tripguide.databinding.ActivityMainBinding
import com.example.tripguide.fragment.*
import com.example.tripguide.fragment.dialogfragment.DepartRegionFragment
import com.example.tripguide.fragment.dispositionfragment.*
import com.example.tripguide.utils.Constants
import com.kakao.util.maps.helper.Utility


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
    private val selectCafeFragment = SelectCafeFragment()
    private val selectHotelFragment = SelectHotelFragment()
    private val recommendedTripFragment = RecommendedTripFragment()
    private val festivalFragment = FestivalFragment()
    private val selectFestivalFragment = SelectFestivalFragment()
    private val selectTripFragment = SelectTripFragment()

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
        Log.d(TAG, "SDK : "+Build.VERSION.SDK_INT)

    }

    fun addFragment(now: Fragment, next: Fragment) {
        Log.d(TAG, "$now -> $next")
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, next)
            .setCustomAnimations(R.anim.horizon_enter,
                R.anim.none,
                R.anim.none,
                R.anim.horizon_exit)
            .hide(now)
            .show(next)
            .addToBackStack(null)
            .commit()
    }

    fun removeFragment(show: Fragment) {
        Log.d(TAG, "Remove current Fragment and show $show")
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        supportFragmentManager.beginTransaction()
            .remove(fragment!!)
            .show(dispositionFragment)
            .commit()
    }

    fun dialogFragment(show: DialogFragment) {
        Log.d(TAG, "show $show")
        var dialog = show
        dialog.show(supportFragmentManager, "$show")
    }

    // supportFragmentManager function for fragment transaction
    fun changeFragment(index: Int?) {
        val transaction = supportFragmentManager.beginTransaction()
        when (index) {
            4 -> {
                Log.d(Constants.TAG, "Replace to MainFragment")
                transaction
                    .replace(R.id.fragment_container_view, mainFragment)
                    .show(mainFragment)
                    .commit()
            }
        }
    }
}
