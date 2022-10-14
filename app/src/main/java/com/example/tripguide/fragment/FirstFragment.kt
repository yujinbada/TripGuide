/*
 This fragment is the first fragment the user sees.
 It contains the code for google login
*/
package com.example.tripguide.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.databinding.FragmentDispositionBinding
import com.example.tripguide.databinding.FragmentFirstBinding
import com.example.tripguide.model.FirebaseClass
import com.example.tripguide.utils.Constants.TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FirstFragment : Fragment() {
    private lateinit var auth: FirebaseAuth // shared instance of object
    private lateinit var firestore : FirebaseFirestore
    private val RC_SIGN_IN = 9001
    private var googleSignInClient: GoogleSignInClient?=null

    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentFirstBinding? = null
    private val binding get() = mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FirstFragment - onViewCreated() called")
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("156500920502-aeb7fr9gq9elckm9l7ceobushc2qp67f.apps.googleusercontent.com")
            .requestEmail()
            .build()

        var googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.signingoogle.setOnClickListener {
            Log.d(TAG, "MainActivity login button clicked")
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{ // If we succeeded login
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {  // If we failed login
                Log.d(TAG, "login failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task -> // Register on Firebase
                if (task.isSuccessful) { // Succeeded
                    Log.d(TAG, "login succeeded")
                    val user = auth.currentUser
                    var userInfo = FirebaseClass()
                    user?.let {
                        userInfo.uid = user.uid
                        userInfo.email = user.email
                    }
                    firestore.collection("user").document(user?.uid.toString()).set(userInfo)
                    Log.d(TAG, user.toString())
                    mainActivity.changeFragment(4)


                } else { // Failed
                    Log.d(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}

