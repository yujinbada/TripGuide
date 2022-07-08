package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import kotlinx.android.synthetic.main.fragment_disposition.*
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

        trans_chip1.setOnClickListener(this)
        trans_chip2.setOnClickListener(this)
        trans_chip3.setOnClickListener(this)
        before_btn2.setOnClickListener(this)
        next_btn2.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.trans_chip1 -> {
                trans_constraintLayout.visibility = View.INVISIBLE
            }
            R.id.trans_chip2 -> {
                trans_constraintLayout.visibility = View.INVISIBLE
            }
            R.id.trans_chip3 -> {
                trans_constraintLayout.visibility = View.VISIBLE
            }
            R.id.next_btn2 -> {
                if (trans_chip3.isChecked) {
                    mainActivity.changeFragment(7)
                } else if (trans_chip1.isChecked || trans_chip2.isChecked) {
                    mainActivity.changeFragment(8)
                } else {
                    Toast.makeText(activity, "이동수단을 선택해 주세요!", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.before_btn2 -> {
                mainActivity.changeFragment(6)
            }
        }
    }
}