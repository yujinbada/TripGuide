package com.example.tripguide.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tripguide.R
import com.example.tripguide.utils.Constants.TAG
import kotlinx.android.synthetic.main.fragment_disposition.*
import kotlinx.android.synthetic.main.layout_recycler_item.*

class DispositionFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_disposition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        view_depart.setOnClickListener(this)
        view_arriv.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.view_depart -> {
                navController.navigate(R.id.action_dispositionFragment_to_departRegionFragment)
                Log.d(TAG, "출발지 입력으로 이동")
                setFragmentResultListener("requestKey") { key, bundle ->
                    val result = bundle.getString("bundleKey")
                    view_depart.text = result
                }
                val hint = "어디에서 여행을 시작하세요?"
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
            }
            R.id.view_arriv -> {
                navController.navigate(R.id.action_dispositionFragment_to_departRegionFragment)
                Log.d(TAG, "도착지 입력으로 이동")
                setFragmentResultListener("requestKey") { key, bundle ->
                    val result = bundle.getString("bundleKey")
                    view_arriv.text = result
                }
                val hint = "어디로 여행를 가시나요?"
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))

            }
        }

    }
}