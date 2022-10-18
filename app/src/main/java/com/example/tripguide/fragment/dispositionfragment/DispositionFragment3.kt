/*
    Copyright (c).2022.4  the phoenix https://soeun-87.tistory.com/34?category=897414
    This fragment adds the places the user has previously wanted to go to according to each type.
    The added place is shown in the recycler view, and creation and addition are free.
*/
package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.graphics.Color
import android.os.BugreportManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.TripGuide
import com.example.tripguide.adapter.RecommendRecyclerAdapter
import com.example.tripguide.adapter.SelectRecyclerAdapter
import com.example.tripguide.databinding.FragmentDisposition3Binding
import com.example.tripguide.fragment.fbAuth
import com.example.tripguide.fragment.fbFirestore
import com.example.tripguide.model.SelectItem
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.utils.Constants.TAG


class DispositionFragment3 : Fragment(), View.OnClickListener {

    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var mBinding: FragmentDisposition3Binding? = null
    private val binding get() = mBinding!!
    private lateinit var viewModel: SelectViewModel
    private lateinit var adapter1: SelectRecyclerAdapter
    private lateinit var adapter2: SelectRecyclerAdapter
    private lateinit var adapter3: SelectRecyclerAdapter
    var btn_number = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "DispositionFragment3 - onCreateView() called")
        // View Model 설정
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            SelectViewModel::class.java)

        mBinding = FragmentDisposition3Binding.inflate(inflater, container, false)

        // 관광지는 viewModel 의 tourList 로 들어가게 하고 RecyclerView 띄워줌
        var tourList = viewModel.tourList.value
        adapter1 = SelectRecyclerAdapter(tourList?: emptyList<SelectItem>(), onClickDeleteButton={ viewModel.deleteTask(it)})
        adapter1.setHasStableIds(true)
        binding.rvtour.adapter = adapter1
        binding.rvtour.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        // 식당 / 카페는 viewModel 의 foodList 로 들어가게 하고 RecyclerView 띄워줌
        var foodList = viewModel.foodList.value
        adapter2 = SelectRecyclerAdapter(foodList?: emptyList<SelectItem>(), onClickDeleteButton={ viewModel.deleteTask(it)})
        adapter2.setHasStableIds(true)
        binding.rvfood.adapter = adapter2
        binding.rvfood.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        // 숙소는 viewModel 의 hotelList 로 들어가게 하고 RecyclerView 띄워줌
        var hotelList = viewModel.hotelList.value
        adapter3 = SelectRecyclerAdapter(hotelList?: emptyList<SelectItem>(), onClickDeleteButton={ viewModel.deleteTask(it)})
        adapter3.setHasStableIds(true)
        binding.rvhotel.adapter = adapter3
        binding.rvhotel.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchbtn1.setOnClickListener(this)
        binding.searchbtn2.setOnClickListener(this)
        binding.searchbtn3.setOnClickListener(this)
        binding.nextbtn3.setOnClickListener(this)
        binding.beforebtn3.setOnClickListener(this)

        // viveModel 의 selectList 를 observe 하고 있다가 각 데이터의 type 값에 따라 각 adapter 에 post 한다.
        viewModel.selectList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "DispositionFragment3 - viewModel observe is called")
            binding.rvtour.post(Runnable { adapter1.setTourData(it.filter { x -> x.type == 12} as ArrayList<SelectItem>) })
            binding.rvfood.post(Runnable { adapter2.setFoodData(it.filter { x -> x.type == 39} as ArrayList<SelectItem>) })
            binding.rvhotel.post(Runnable { adapter3.setHotelData(it.filter { x -> x.type == 32} as ArrayList<SelectItem>) })
        })
    }

    override fun onClick(v: View?) {
        var userInfo = TripGuide()
        userInfo.uid = fbAuth?.uid
        userInfo.userId = fbAuth?.currentUser?.email
        userInfo.timestamp = System.currentTimeMillis()
        userInfo.must_sights = binding.rvtour.itemDecorationCount.toString()
        userInfo.must_food = binding.rvfood.itemDecorationCount.toString()
        userInfo.must_hotel = binding.rvhotel.itemDecorationCount.toString()

        fbFirestore?.collection("must_visit_place")?.document(fbAuth?.uid.toString())?.set(userInfo)


        when(v?.id) {
            R.id.searchbtn1 -> {
                btn_number = 1
                val hint = "가고 싶은 관광지를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                setFragmentResult("numberrequestKey", bundleOf("numberbundleKey" to 12))
                mainActivity.addFragment(DispositionFragment3(), DispositionFragment4())
            }
            R.id.searchbtn2 -> {
                btn_number = 2
                val hint = "가고 싶은 식당/카페를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                setFragmentResult("numberrequestKey", bundleOf("numberbundleKey" to 39))
                mainActivity.addFragment(DispositionFragment3(), DispositionFragment4())
            }
            R.id.searchbtn3 -> {
                btn_number = 3
                val hint = "가고 싶은 숙소를 선택해 주세요."
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
                setFragmentResult("numberrequestKey", bundleOf("numberbundleKey" to 32))
                mainActivity.addFragment(DispositionFragment3(), DispositionFragment4())
            }
            R.id.nextbtn3 -> {
                mainActivity.addFragment(DispositionFragment3(), DispositionFragment5())
            }
            R.id.beforebtn3 -> {
                mainActivity.removeFragment(DispositionFragment2())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}