package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tripguide.MainActivity
import com.example.tripguide.databinding.FragmentDisposition6Binding
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.model.TourRoute
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi.Companion.BASE_URL

class DispositionFragment6 : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentDisposition6Binding? = null
    private val binding get() = mBinding!!
    private lateinit var viewModel: SelectViewModel

    private val routeList = ArrayList<TourRoute>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "DispositionFragment6 - onCreateView() called")
        // View Model 설정
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            SelectViewModel::class.java)

        mBinding = FragmentDisposition6Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var tourList = viewModel.tourList.value
        var foodList = viewModel.foodList.value
        var hotelList = viewModel.hotelList.value
        var departRegion = viewModel.departRegion.value
        var departStationList = viewModel.departStationList.value
        var arriveStationList = viewModel.arriveStationList.value

        Log.d(TAG, "tourList - $tourList")
        tourList?.map {
            routeList.add(TourRoute(it.title, "Tour", it.mapX, it.mapY))
        }
        foodList?.map {
            routeList.add(TourRoute(it.title, "food", it.mapX, it.mapY))
        }
        hotelList?.map {
            routeList.add(TourRoute(it.title, "hotel", it.mapX, it.mapY))
        }

        BASE_URL

    }

    override fun onClick(v: View?) {
    }

}