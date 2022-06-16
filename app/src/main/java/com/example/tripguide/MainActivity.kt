package com.example.tripguide

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.tripguide.databinding.ActivityMainBinding
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
    lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth //객체의 공유 인스턴스

    private val RC_SIGN_IN = 9001

    private var googleSignInClient:GoogleSignInClient?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("156500920502-aeb7fr9gq9elckm9l7ceobushc2qp67f.apps.googleusercontent.com")
            .requestEmail()
            .build()
        var googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInGoogleBtn:SignInButton = findViewById(R.id.sign_in_google)

        signInGoogleBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        Log.d(TAG, "MainActivity - onCreate() called")
        navController = nav_host_fragment.findNavController()
        fun Activity.setStatusBarTransparent() {
            window.setFlags( // 상태바 투명화 함수
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{ //로그인 성공할 경우
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
                navController.navigate(R.id.action_firstFragment_to_mainFragment)
                Log.d(TAG, "로그인 성공")
            } catch (e: ApiException) {  //로그인 실패 할 경우
                Log.d(TAG, "로그인 실패")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task -> //파이어 베이스에 등록
                if (task.isSuccessful) { //성공
                    val user = auth.currentUser
                    Log.d(TAG, user.toString())

                } else { //실패
                    Log.d(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

}



