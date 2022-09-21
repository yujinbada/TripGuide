package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
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
import com.example.tripguide.fragment.FirstFragment
import com.example.tripguide.fragment.recommend.RecommendFragment1
import com.example.tripguide.fragment.recommend.RecommendFragment2
import com.example.tripguide.fragment.recommend.RecommendFragment3
import com.google.android.material.tabs.TabLayout
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

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDisposition5Binding.inflate(inflater, container, false)
        viewPager = view!!.findViewById(R.id.viewPager)
        tabLayout = view!!.findViewById(R.id.tabLayout)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recommendAdapter = RecommendAdapter(requireActivity())
        // 3개의 Fragment Add
        recommendAdapter.addFragment(RecommendFragment1())
        recommendAdapter.addFragment(RecommendFragment2())
        recommendAdapter.addFragment(RecommendFragment3())
        // Adapter
        viewPager.adapter = recommendAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")
            }
        })

        // TabLayout attach
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when(position){
                1 -> tab.text = "관광지"
                2 -> tab.text = "식당 / 카페"
                3 -> tab.text = "숙소"
            }
        }.attach()
    }
}