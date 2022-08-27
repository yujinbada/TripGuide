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
import com.example.tripguide.fragment.fbAuth
import com.example.tripguide.fragment.fbFirestore
import kotlinx.android.synthetic.main.fragment_disposition2.*

class DispositionFragment2 : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disposition2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrival_car.setOnClickListener(this)
        arrival_public.setOnClickListener(this)
        arrival_plane.setOnClickListener(this)
        before_btn2.setOnClickListener(this)
        next_btn2.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        var userInfo = TripGuide()
        userInfo.uid = fbAuth?.uid
        userInfo.userId = fbAuth?.currentUser?.email
        userInfo.timestamp = System.currentTimeMillis()
        userInfo.arrival_how = arrival_how.checkedChipId.toString()

        fbFirestore?.collection("arrival_how")?.document(fbAuth?.uid.toString())?.set(userInfo)


        when(v?.id) {
            R.id.arrival_car -> {
                trans_constraintLayout.visibility = View.INVISIBLE
            }
            R.id.arrival_public -> {
                trans_constraintLayout.visibility = View.INVISIBLE
            }
            R.id.arrival_plane -> {
                trans_constraintLayout.visibility = View.VISIBLE
            }
            R.id.next_btn2 -> {
                if (arrival_plane.isChecked) {
                    mainActivity.changeFragment(7)
                } else if (arrival_car.isChecked || arrival_public.isChecked) {
                    mainActivity.changeFragment(8)
                } else {
                    Toast.makeText(activity, "이동수단을 선택해 주세요!", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.before_btn2 -> {
                mainActivity.changeFragment(66)
            }
        }
    }
}