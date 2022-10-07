package com.example.tripguide.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tripguide.MainActivity
import com.example.tripguide.adapter.SelectTourImageAdapter
import com.example.tripguide.databinding.FragmentSelectTourBinding
import com.example.tripguide.model.RecommendItem

class SelectTourFragment : Fragment() {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentSelectTourBinding? = null
    private val binding get() = mBinding!!
    private var arrayList = ArrayList<RecommendItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
            mBinding = FragmentSelectTourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val bundle = arguments
//        arrayList = bundle!!.getParcelableArrayList<Parcelable>("list") as RecommendItem
//
//        binding.viewPager2.adapter = SelectTourImageAdapter
//        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

}