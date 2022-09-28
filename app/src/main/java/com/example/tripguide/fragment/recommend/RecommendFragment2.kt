package com.example.tripguide.fragment.recommend

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripguide.MainActivity

import com.example.tripguide.databinding.FragmentRecommend2Binding

class RecommendFragment2 : Fragment() {
    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentRecommend2Binding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentRecommend2Binding.inflate(inflater, container, false)
        return binding.root
    }

    fun newInstant() : RecommendFragment2
    {
        val args = Bundle()
        val frag = RecommendFragment2()
        frag.arguments = args
        return frag
    }
}