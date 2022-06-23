package com.example.tripguide

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.tripguide.databinding.ActivityMainBinding
import com.example.tripguide.fragment.DepartRegionFragment
import com.example.tripguide.fragment.DispositionFragment
import com.example.tripguide.fragment.FirstFragment
import com.example.tripguide.fragment.MainFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
                Log.d(TAG, "MainActivity - changeFragment() called")
                transaction.replace(R.id.fragment_container_view, dispositionFragment)
                    .show(dispositionFragment)
                    .remove(firstFragment)
                    .addToBackStack(null)
                    .commit()
            }
            2 -> {
                Log.d(TAG, "MainActivity - changeFragment() called")
                transaction.replace(R.id.fragment_container_view, departRegionFragment)
                    .show(departRegionFragment)
                    .hide(dispositionFragment)
                    .addToBackStack(null)
                    .commit()
            }

            3 -> {
                Log.d(TAG, "MainActivity - changeFragment() called")
                transaction.show(dispositionFragment)
                .remove(departRegionFragment)
                .addToBackStack(null)
                .commit()
            }

            4 -> {
                Log.d(TAG, "MainActivity - changeFragment() called")
                transaction.replace(R.id.fragment_container_view, mainFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

}




