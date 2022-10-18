package com.example.tripguide.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.TripGuide
import com.example.tripguide.databinding.FragmentDispositionBinding
import com.example.tripguide.databinding.FragmentMainBinding
import com.example.tripguide.fragment.dispositionfragment.DispositionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

var fbAuth : FirebaseAuth?= null
var fbFirestore : FirebaseFirestore?= null

class MainFragment : Fragment(), View.OnClickListener {

    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var mBinding: FragmentMainBinding? = null
    private val binding get() = mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnplan.setOnClickListener(this)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnplan -> {
                mainActivity.addFragment(MainFragment(), DispositionFragment())
            }
        }
    }
    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}