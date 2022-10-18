package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.TripGuide
import com.example.tripguide.databinding.FragmentDisposition2Binding
import com.example.tripguide.databinding.FragmentDispositionBinding
import com.example.tripguide.fragment.fbAuth
import com.example.tripguide.fragment.fbFirestore


class DispositionFragment2 : Fragment(), View.OnClickListener {
    private var mBinding: FragmentDisposition2Binding? = null
    private val binding get() = mBinding!!

    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentDisposition2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrivalcar.setOnClickListener(this)
        binding.arrivalpublic.setOnClickListener(this)
        binding.arrivalplane.setOnClickListener(this)
        binding.beforebtn2.setOnClickListener(this)
        binding.nextbtn2.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        var userInfo = TripGuide()
        userInfo.uid = fbAuth?.uid
        userInfo.userId = fbAuth?.currentUser?.email
        userInfo.timestamp = System.currentTimeMillis()
        userInfo.arrival_how = binding.arrivalhow.checkedChipId.toString()

        fbFirestore?.collection("arrival_how")?.document(fbAuth?.uid.toString())?.set(userInfo)


        when(v?.id) {
            R.id.arrivalcar -> {
            }
            R.id.arrivalpublic -> {
            }
            R.id.arrivalplane -> {
            }
            R.id.nextbtn2 -> {
                if (binding.arrivalplane.isChecked) {
                    mainActivity.addFragment(DispositionFragment2(), DispositionFragment222())
                } else if (binding.arrivalcar.isChecked || binding.arrivalpublic.isChecked) {
                    mainActivity.addFragment(DispositionFragment2(), DispositionFragment3())
                } else {
                    Toast.makeText(activity, "이동수단을 선택해 주세요!", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.beforebtn2 -> {
                mainActivity.removeFragment(DispositionFragment())
            }
        }
    }
    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}