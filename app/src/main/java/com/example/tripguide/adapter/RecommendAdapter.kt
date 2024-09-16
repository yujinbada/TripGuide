package com.example.tripguide.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tripguide.fragment.recommend.RecommendFragment1
import com.example.tripguide.fragment.recommend.RecommendFragment2
import com.example.tripguide.fragment.recommend.RecommendFragment3


class RecommendAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RecommendFragment1()
            1 -> RecommendFragment2()
            else -> RecommendFragment3()
        }
    }
}