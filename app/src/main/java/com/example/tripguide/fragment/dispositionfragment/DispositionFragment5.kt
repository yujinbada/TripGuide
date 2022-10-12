package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.adapter.RecommendAdapter
import com.example.tripguide.databinding.FragmentDisposition5Binding
import com.google.android.material.tabs.TabLayoutMediator


class DispositionFragment5 : Fragment() {
    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentDisposition5Binding? = null
    private val binding get() = mBinding!!
    private val tabTextList = listOf("관광지", "식당 / 카페", "숙소")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDisposition5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = RecommendAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

    }

}