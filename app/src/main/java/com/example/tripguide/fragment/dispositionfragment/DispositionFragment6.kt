package com.example.tripguide.fragment.dispositionfragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.databinding.FragmentDisposition6Binding
import com.example.tripguide.model.*
import com.example.tripguide.model.kakaoroute.Destination
import com.example.tripguide.model.kakaoroute.KakaoRoute
import com.example.tripguide.model.kakaoroute.Origin
import com.example.tripguide.retrofit.RetrofitRoute
import com.example.tripguide.fragment.RecommendedTripFragment
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalTime
import kotlin.collections.ArrayList

class DispositionFragment6 : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentDisposition6Binding? = null
    private val binding get() = mBinding!!
    private lateinit var viewModel: SelectViewModel

    private var origin = Origin("", 0.0, 0.0)

    private val tourDestination = ArrayList<Destination>()
    private val foodDestination = ArrayList<Destination>()
    private val hotelDestination = ArrayList<Destination>()

    private val responseRouteList = ArrayList<RouteResult>()


    private val finalRoute = ArrayList<SelectItem>()


    var departStationTime : LocalTime = LocalTime.of(0, 0, 0)
    var arriveStationTime : LocalTime = LocalTime.of(0, 0, 0)

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
    // 여행의 시작의 기본 시간을 오전 8시로 설정
    var startTime : LocalTime = LocalTime.of(8, 0, 0)
    var liveTime : LocalTime = LocalTime.of(8, 0, 0)
    var stationDurationTime: LocalTime = LocalTime.of(0, 0, 0)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.beforebtn6.setOnClickListener(this)
        clickShareBtn()

        val tourList = viewModel.tourList.value
        val foodList = viewModel.foodList.value
        val hotelList = viewModel.hotelList.value
        val departRegion = viewModel.departRegion.value
        val departStationList = viewModel.departStationList.value
        val arriveStationList = viewModel.arriveStationList.value
        if(viewModel.departStationTime.value != null) {
            departStationTime = viewModel.departStationTime.value!!
        }
        if(viewModel.arriveStationTime.value != null) {
            arriveStationTime = viewModel.arriveStationTime.value!!
        }

        // 여행의 출발주소를 origin 으로 설정
        departRegion?.forEach {
            origin.name = it.title
            origin.x = it.mapX?.toDouble()
            origin.y = it.mapY?.toDouble()
            Log.d(TAG, "origin - $origin")
            finalRoute.add(it)
        }

        /* 비행기 or 기차를 이용해서 공항이나 역을 가야하면 origin 에서 공항이나 역까지 거리를 getResultSearch 로 구하고
           이전에 tourDestination 에 집어넣었던 공항이나 역값을 제거하고 공항이나 역을 finalRoute 에 add 한다. */
        if (!departStationList?.isEmpty()!!) {
            Log.d(TAG, "departStationList - $departStationList")
            tourDestination.add(Destination(departStationList.first().title,
                departStationList.first().mapX?.toDouble(),
                departStationList.first().mapY?.toDouble()
                ))

            addFinalRoute(getResultSearch(origin, tourDestination.first()), tourDestination, departStationList)

            /* 비행기 or 기차를 이용해서 공항이나 역을 가야하면 도착역을 여행지에서의 일정의 첫번째로 설정하고
               도착시간에서 출발시간을 뺀 값을 초 단위로 duration 에 넣어준다. */
            Log.d(TAG, "arriveStationList - $arriveStationList")
            Log.d(TAG, "departStationTime - $departStationTime, arriveStationTime - $arriveStationTime")
            arriveStationList?.forEach {
                liveTime = LocalTime.of(arriveStationTime.hour, arriveStationTime.minute, 0)
                stationDurationTime = arriveStationTime.minusHours(departStationTime.hour.toLong())
                    .minusMinutes(departStationTime.minute.toLong())
                it.duration = stationDurationTime.hour * 60 * 60 + stationDurationTime.minute * 60
                Log.d(TAG, "stationDurationTime - $stationDurationTime")
                finalRoute.add(it)
                origin.name = it.title
                origin.x = it.mapX?.toDouble()
                origin.y = it.mapY?.toDouble()
            }
        }


        /* 여행지, 식당, 숙소를 입력받은 viewModel 을 경로 계산을 위한 배열 영식으로 바꿔서 넣어준다. */
        tourList?.map {
            tourDestination.add(Destination(it.title, it.mapX?.toDouble(), it.mapY?.toDouble()))
        }
        foodList?.map {
            foodDestination.add(Destination(it.title, it.mapX?.toDouble(),
                it.mapY?.toDouble()))
        }
        hotelList?.map {
            hotelDestination.add(Destination(it.title, it.mapX?.toDouble(), it.mapY?.toDouble()))
        }

        val count = tourList?.count()!! + foodList?.count()!! + hotelList?.count()!!
        var breakFast = 0
        var lunch = 0
        var dinner = 0

        /* Origin 과 여행지 목록을 바탕으로 finalRoute 를 계산한다. */
        for (i in 1..count) {
            /* liveTime 을 기준으로 */
            when(liveTime.hour * 60 + liveTime.minute) {
                in 0..420 -> { // 0시부터 7시에는 여행 시작
                    Log.d(TAG, "tourDestination - $tourDestination")
                }
                in 421..540 -> { // 7시부터 9시는 아침 식사 시간
                    Log.d(TAG, "foodDestination - $foodDestination")
                    addFinalRoute(getResultSearch(origin, tourDestination.first()), tourDestination, departStationList)
                    breakFast++
                }
                in 541..720 -> { // 9시부터 12시는 여행 시간
                    if(i == 1 || breakFast == 1) {
                        Log.d(TAG, "tourDestination - $tourDestination")
                    }
                    else {

                    }
                }
                in 721..840 -> { // 12시부터 2시는 점심 시간
                    Log.d(TAG, "tourDestination - $tourDestination")
                }
                in 841..1200 -> { // 2시부터 6시는 여행 시간
                    Log.d(TAG, "tourDestination - $tourDestination")
                }
                in 1201..1320 -> { // 6시부터 8시는 저녁 시간
                    Log.d(TAG, "foodDestination - $foodDestination")
                }
                else -> { // 8시 이후는 숙소
                    Log.d(TAG, "hotelDestination - $hotelDestination")
                }
            }
        }
    }

    // Origin 에 가장 가까운 Destination Return
    private fun getResultSearch(origin: Origin, destination: Destination) : RouteResult {
        val retrofit = Retrofit.Builder()
            .baseUrl( "https://apis-navi.kakaomobility.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RetrofitRoute::class.java)
        val call = api.getKakaoRoute(KakaoApi.API_KEY, origin, destination, "TIME", true)
        var nextLocation = RouteResult(null, null)

        call.enqueue(object : Callback<KakaoRoute> {
            override fun onResponse(call: Call<KakaoRoute>, response: Response<KakaoRoute>) {
                Log.d(TAG, "communication success")
                Log.d(TAG, "response.body() - ${response.body()}")
                nextLocation = addItems(response.body())
            }

            override fun onFailure(call: Call<KakaoRoute>, t: Throwable) {
                Log.d(TAG, "error : " + t.message)
            }
        })
        return nextLocation
    }


    private fun addItems(searchResult: KakaoRoute?) : RouteResult {
        val nextLocation = RouteResult(null, null)
        if (!searchResult?.routes.isNullOrEmpty()) {
            // Search results available
            // 우리는 Origin 에서 가장 가까운 지역만 알면 되기 때문에 첫번째 값만 가진다.
            if (searchResult!!.routes.first().result_msg != "길찾기 성공") {
                nextLocation.key = searchResult.routes.first().summary.origin.name
                nextLocation.duration = searchResult.routes[0].summary.duration
                Log.d(TAG, "nextLocation - $nextLocation")
            }
            else Log.d(TAG, "길찾기 실패")
        }
        return nextLocation
    }

    private fun addFinalRoute(nextLocation: RouteResult, destinations: ArrayList<Destination>, list: List<SelectItem>?) {
        destinations.map { x ->
            if( x.name == nextLocation.key ){
                list?.map { y ->
                    if(y.title == x.name) {
                        y.duration = nextLocation.duration
                        liveTime.plusSeconds(nextLocation.duration!!.toLong())
                        liveTime.plusMinutes(120)
                        Log.d(TAG, "liveTime - $liveTime")
                        Log.d(TAG, "finalRoute - $finalRoute")
                        finalRoute.add(y)
                    }
                }
                destinations.remove(x)
            }
        }
    }

    private fun clickShareBtn(){
        binding.btnshare.setOnClickListener {
            try {
                val sendText = "TripGuide 공유하기"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText)
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Share"))
            } catch (ignored: ActivityNotFoundException) {
                Log.d("test", "ignored : $ignored")
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.beforebtn6 -> {
                mainActivity.removeFragment(RecommendedTripFragment())
            }
        }
    }


}