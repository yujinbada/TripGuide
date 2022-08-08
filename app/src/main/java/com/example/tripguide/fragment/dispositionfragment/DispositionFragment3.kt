package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import kotlinx.android.synthetic.main.fragment_disposition.*
import kotlinx.android.synthetic.main.fragment_disposition3.*
import kotlinx.android.synthetic.main.layout_recycler_item.*

class DispositionFragment3 : Fragment(), View.OnClickListener {
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
        return inflater.inflate(R.layout.fragment_disposition3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_btn_1.setOnClickListener(this)
        search_btn_2.setOnClickListener(this)
        search_btn_3.setOnClickListener(this)
        next_btn3.setOnClickListener(this)
        before_btn3.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.search_btn_1 -> {
                val hint = "가고 싶은 관광지를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                val contentTypeId = 12
                setFragmentResult("typerequestKey", bundleOf("typebundlekey" to contentTypeId))
                mainActivity.changeFragment(10)
            }
            R.id.search_btn_2 -> {
                val hint = "가고 싶은 식당/카페를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                val contentTypeId = 39
                setFragmentResult("typerequestKey", bundleOf("typebundlekey" to contentTypeId))
                mainActivity.changeFragment(10)
            }
            R.id.search_btn_3 -> {
                val hint = "가고 싶은 숙소를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                val contentTypeId = 32
                setFragmentResult("typerequestKey", bundleOf("typebundlekey" to contentTypeId))
                mainActivity.changeFragment(10)
            }
            R.id.next_btn3 -> {

            }
            R.id.before_btn3 -> {

            }
        }

    }
}