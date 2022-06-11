package fragment.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tripguide.R
import com.example.tripguide.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment() {
    val TAG: String = "로그"
    private lateinit var auth: FirebaseAuth //객체의 공유 인스턴스
    private val RC_SIGN_IN = 9001
    private var googleSignInClient: GoogleSignInClient? = null
    lateinit var navController: NavController //네비게이션 컨트롤러 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "FirstFragment - onCreateView() called")


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Fragment 이용 시
        val root = inflater.inflate(R.layout.fragment_first, container, false)
        // Activity 이용 시 root.context 대신 this이용

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(root.context, gso)

        val google_login = root.findViewById<SignInButton>(R.id.sign_in_google)
        google_login.setOnClickListener { // google login button
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 구글 로그인
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    navController.navigate(R.id.action_firstFragment_to_mainFragment)
                    val user = auth.currentUser
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
}

