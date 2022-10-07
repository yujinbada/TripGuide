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
import com.example.tripguide.databinding.FragmentDisposition22Binding
import com.example.tripguide.fragment.fbAuth
import com.example.tripguide.fragment.fbFirestore

class DispositionFragment22 : Fragment(), View.OnClickListener {

    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentDisposition22Binding? = null
    private val binding get() = mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentDisposition22Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.departurecar.setOnClickListener(this)
        binding.departurepublic.setOnClickListener(this)
        binding.transcar.setOnClickListener(this)
        binding.transpublic.setOnClickListener(this)
        binding.nextbtn22.setOnClickListener(this)
        binding.beforebtn22.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var userInfo = TripGuide()
        userInfo.uid = fbAuth?.uid
        userInfo.userId = fbAuth?.currentUser?.email
        userInfo.timestamp = System.currentTimeMillis()
        userInfo.departure_how = binding.departurehow.checkedChipId.toString()
        userInfo.transportation = binding.transportation.checkedChipId.toString()

        fbFirestore?.collection("departure_how")?.document(fbAuth?.uid.toString())?.set(userInfo)


        when(v?.id) {
            R.id.departurecar -> {
                binding.transconstraintLayout2.visibility = View.VISIBLE
            }
            R.id.departurepublic -> {
                binding.transconstraintLayout2.visibility = View.VISIBLE
            }
            R.id.transcar -> {

            }
            R.id.transpublic -> {

            }
            R.id.nextbtn22 -> {
                if ((binding.departurecar.isChecked || binding.departurepublic.isChecked) && (binding.transcar.isChecked || binding.transpublic.isChecked)) {
                    mainActivity.changeFragment(8)
                } else
                    Toast.makeText(activity, "이동수단을 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }
            R.id.beforebtn22 -> {
                mainActivity.changeFragment(9)
            }
        }
    }
    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}