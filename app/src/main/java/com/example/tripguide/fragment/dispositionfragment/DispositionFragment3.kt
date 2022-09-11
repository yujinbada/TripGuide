package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.TripGuide
import com.example.tripguide.databinding.FragmentDisposition3Binding
import com.example.tripguide.fragment.fbAuth
import com.example.tripguide.fragment.fbFirestore
import com.example.tripguide.model.SelectItem


class DispositionFragment3 : Fragment(), View.OnClickListener {

    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var chipList: ArrayList<SelectItem> = arrayListOf<SelectItem>()
    private var mBinding: FragmentDisposition3Binding? = null
    private val binding get() = mBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        mBinding = FragmentDisposition3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchbtn1.setOnClickListener(this)
        binding.searchbtn2.setOnClickListener(this)
        binding.searchbtn3.setOnClickListener(this)
        binding.nextbtn3.setOnClickListener(this)
        binding.beforebtn3.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        var userInfo = TripGuide()

        fbFirestore?.collection("must_visit_place")?.document(fbAuth?.uid.toString())?.set(userInfo)


        when(v?.id) {
            R.id.searchbtn1 -> {
                val hint = "가고 싶은 관광지를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                val contentTypeId = 12
                setFragmentResult("typerequestKey", bundleOf("typebundlekey" to contentTypeId))
                mainActivity.changeFragment(10)
            }
            R.id.searchbtn2 -> {
                val hint = "가고 싶은 식당/카페를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                val contentTypeId = 39
                setFragmentResult("typerequestKey", bundleOf("typebundlekey" to contentTypeId))
                mainActivity.changeFragment(10)
            }
            R.id.searchbtn3 -> {
                val hint = "가고 싶은 숙소를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                val contentTypeId = 32
                setFragmentResult("typerequestKey", bundleOf("typebundlekey" to contentTypeId))
                mainActivity.changeFragment(10)
            }
            R.id.nextbtn3 -> {

            }
            R.id.beforebtn3 -> {

            }
        }

    }

    fun createcardview() {
        val data = activity?.intent?.getParcelableArrayListExtra<SelectItem>("list") as SelectItem

    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}