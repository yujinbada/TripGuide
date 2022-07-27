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
        trans_chip2_1.setOnClickListener(this)
        trans_chip2_2.setOnClickListener(this)
        trans_chip2_3.setOnClickListener(this)
        trans_chip2_4.setOnClickListener(this)
        next_btn22.setOnClickListener(this)
        before_btn22.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.trans_chip2_1 -> {
                trans_constraintLayout2.visibility = View.VISIBLE
            }
            R.id.trans_chip2_2 -> {
                trans_constraintLayout2.visibility = View.VISIBLE
            }
            R.id.trans_chip2_3 -> {

            }
            R.id.trans_chip2_4 -> {

            }
            R.id.next_btn22 -> {
                if ((trans_chip2_1.isChecked || trans_chip2_2.isChecked) && (trans_chip2_3.isChecked || trans_chip2_4.isChecked)) {
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