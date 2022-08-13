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
import kotlinx.android.synthetic.main.fragment_disposition22.*

class DispositionFragment22 : Fragment(), View.OnClickListener {
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
        return inflater.inflate(R.layout.fragment_disposition22, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        departure_car.setOnClickListener(this)
        departure_public.setOnClickListener(this)
        trans_chip2_3.setOnClickListener(this)
        trans_chip2_4.setOnClickListener(this)
        next_btn22.setOnClickListener(this)
        before_btn22.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var userInfo = TripGuide()
        userInfo.uid = fbAuth?.uid
        userInfo.userId = fbAuth?.currentUser?.email
        userInfo.timestamp = System.currentTimeMillis()
        userInfo.departure_how = departure_how.checkedChipId.toString()

        fbFirestore?.collection("departure_how")?.document(fbAuth?.uid.toString())?.set(userInfo)


        when(v?.id) {
            R.id.departure_car -> {
                trans_constraintLayout2.visibility = View.VISIBLE
            }
            R.id.departure_public -> {
                trans_constraintLayout2.visibility = View.VISIBLE
            }
            R.id.trans_chip2_3 -> {

            }
            R.id.trans_chip2_4 -> {

            }
            R.id.next_btn22 -> {
                if ((departure_car.isChecked || departure_public.isChecked) && (trans_chip2_3.isChecked || trans_chip2_4.isChecked)) {
                    mainActivity.changeFragment(8)
                } else
                    Toast.makeText(activity, "이동수단을 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }
            R.id.before_btn22 -> {
                mainActivity.changeFragment(9)
            }
        }
    }
}