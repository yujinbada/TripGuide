package com.example.tripguide.fragment.dispositionfragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.adapter.FinalRecyclerAdapter1
import com.example.tripguide.adapter.FinalRecyclerAdapter2
import com.example.tripguide.databinding.FragmentDisposition6Binding
import com.example.tripguide.fragment.RecommendedTripFragment
import com.example.tripguide.fragment.recommend.LocationBasedFragment
import com.example.tripguide.model.*
import com.example.tripguide.model.kakaoroute.Destination
import com.example.tripguide.model.kakaoroute.KakaoRoute
import com.example.tripguide.model.kakaoroute.Origin
import com.example.tripguide.retrofit.RetrofitRoute
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi
import kotlinx.coroutines.*
import net.daum.mf.map.api.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalTime


class DispositionFragment6 : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
//        LoadingDialog(mainActivity).show()
    }
    private var mBinding: FragmentDisposition6Binding? = null
    private val binding get() = mBinding!!
    private lateinit var viewModel: SelectViewModel

    private var origin = Origin("", 0.0, 0.0)

    private val tourDestination = ArrayList<Destination>()
    private val foodDestination = ArrayList<Destination>()
    private val hotelDestination = ArrayList<Destination>()
    private var arriveOffice = Destination("", 0.0, 0.0)

    private val responseRouteList = ArrayList<RouteResult>()

    private val finalRoute = ArrayList<FinalItem>()
    private val grandFinalRoute = ArrayList<GrandFinalItem>()
    private val finalRecyclerAdapter = FinalRecyclerAdapter1(grandFinalRoute)

    var departStationTime : LocalTime = LocalTime.of(0, 0, 0)
    var arriveStationTime : LocalTime = LocalTime.of(0, 0, 0)
    // 여행의 시작의 기본 시간을 오전 8시로 설정
    var startTime : LocalTime = LocalTime.of(8,0, 0)
    var liveTime : LocalTime = LocalTime.of(8, 0, 0)
    var stationDurationTime: LocalTime = LocalTime.of(0, 0, 0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "DispositionFragment6 - onCreateView() called")

        // View Model 설정
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
                SelectViewModel::class.java)
        mBinding = FragmentDisposition6Binding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var mapView : MapView
    var start = ""
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.beforebtn6.setOnClickListener(this)
        clickShareBtn()

        mapView = MapView(activity)
        binding.KakaoMapView.addView(mapView)

        binding.routeRCV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.routeRCV.adapter = finalRecyclerAdapter

        val tourList = viewModel.tourList.value
        val foodList = viewModel.foodList.value
        val hotelList = viewModel.hotelList.value
        val departRegion = viewModel.departRegion.value
        val departStationList = viewModel.departStationList.value
        val arriveStationList = viewModel.arriveStationList.value
        var startDate = viewModel.startDate.value
        start = startDate!!
        val endDate = viewModel.endDate.value
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
            it.liveTime = liveTime
            if(viewModel.arriveOfficeRegion.value != null) {
                arriveOffice = viewModel.arriveOfficeRegion.value!!
                Log.d(TAG, "arriveOffice - $arriveOffice")
                val arriveCity = getResultSearch(origin, arriveOffice)
                liveTime = liveTime.plusSeconds(arriveCity.duration?.toLong()!!)
            }

            Log.d(TAG, "origin - $origin")
            finalRoute.add(FinalItem(it, startDate))
        }



        /* 비행기 or 기차를 이용해서 공항이나 역을 가야하면 origin 에서 공항이나 역까지 거리를 getResultSearch 로 구하고
           이전에 tourDestination 에 집어넣었던 공항이나 역값을 제거하고 공항이나 역을 finalRoute 에 add 한다. */
        if (!departStationList?.isEmpty()!!) {
            Log.d(TAG, "departStationList - $departStationList")
            tourDestination.add(Destination(departStationList.first().title,
                departStationList.first().mapX?.toDouble(),
                departStationList.first().mapY?.toDouble()
            ))
            GlobalScope.launch {
                addFinalRoute(getResultSearch(origin, tourDestination.first()), tourDestination, departStationList, startDate)
            }


            /* 비행기 or 기차를 이용해서 공항이나 역을 가야하면 도착역을 여행지에서의 일정의 첫번째로 설정하고
               도착시간에서 출발시간을 뺀 값을 초 단위로 duration 에 넣어준다. */
            Log.d(TAG, "arriveStationList - $arriveStationList")
            Log.d(TAG, "departStationTime - $departStationTime, arriveStationTime - $arriveStationTime")
            arriveStationList?.forEach {
                liveTime = arriveStationTime
                it.liveTime = liveTime
                stationDurationTime = arriveStationTime.minusHours(departStationTime.hour.toLong())
                    .minusMinutes(departStationTime.minute.toLong())
                Log.d(TAG, "stationDurationTime - $stationDurationTime")
                finalRoute.add(FinalItem(it, startDate))
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
        val origindate = startDate
        /* Origin 과 여행지 목록을 바탕으로 finalRoute 를 계산한다. */
        for (i in 1..count) {
            /* liveTime 을 기준으로 */
            var type = ""
            type = when(liveTime.hour * 60 + liveTime.minute) {
                in (0..420) -> "tour" // 0시부터 7시에는 여행 시작
                in 421..540 -> "food" // 7시부터 9시는 아침 식사 시간
                in 541..720 -> "tour" // 9시부터 12시는 여행 시간
                in 721..840 -> "food" // 12시부터 2시는 점심 시간
                in 841..1080 -> "tour" // 2시부터 6시는 여행 시간
                in 1080..1200 -> "food" // 6시부터 8시는 저녁 시간
                else -> "hotel" // 8시 이후는 숙소
            }
            if (i == 1) liveTime = startTime
            when(type) {
                "tour" -> {
                    Log.d(TAG, "liveTime - $liveTime")
                    Log.d(TAG, "tourDestination - $tourDestination")
                    if(tourDestination.isNotEmpty()) {
                        var minLocation = RouteResult(null, 10000000)
                        var nextLocation = RouteResult(null, null)

                        for(destination in tourDestination) {
                            runBlocking {
                                nextLocation = getResultSearch(origin, destination)
                            }

                            if (minLocation.duration!! > nextLocation.duration!!) {
                                minLocation = nextLocation
                            }
                        }

                        Log.d(TAG, "minLocation - $minLocation")
                        tourDestination.map {
                            if(it.name == minLocation.key) {
                                origin = Origin(it.name, it.x, it.y)
                            }
                        }
                        addFinalRoute(minLocation, tourDestination, tourList, startDate)
                        tourList.map {
                            if(it.title == minLocation.key) {
                                tourDestination.remove(Destination(it.title, it.mapX?.toDouble(), it.mapY?.toDouble()))
                            }
                        }
                    }
                    else {
                        Log.d(TAG, "빈 장소 추가")
                        val emptySelectItem = SelectItem(null, "장소 추가", 12,null, null, liveTime)
                        finalRoute.add(FinalItem(emptySelectItem, startDate))
                        liveTime = liveTime.plusHours(2)
                    }
                }
                "food" -> {
                    Log.d(TAG, "liveTime - $liveTime")
                    Log.d(TAG, "foodDestination - $foodDestination")
                    if (foodDestination.isNotEmpty()) {
                        var minLocation = RouteResult(null, 10000000)
                        var nextLocation = RouteResult(null, null)

                        for(destination in foodDestination) {
                            runBlocking {
                                nextLocation = getResultSearch(origin, destination)
                            }

                            if (minLocation.duration!! > nextLocation.duration!!) {
                                minLocation = nextLocation
                            }
                        }

                        Log.d(TAG, "minLocation - $minLocation")
                        foodDestination.map {
                            if(it.name == minLocation.key) {
                                origin = Origin(it.name, it.x, it.y)
                            }
                        }
                        addFinalRoute(minLocation, foodDestination, foodList, startDate)
                        foodList.map {
                            if(it.title == minLocation.key) {
                                foodDestination.remove(Destination(it.title, it.mapX?.toDouble(), it.mapY?.toDouble()))
                            }
                        }
                    }
                    else {
                        Log.d(TAG, "빈 장소 추가")
                        val emptySelectItem = SelectItem(null, "장소 추가", 39,null, null, liveTime)
                        finalRoute.add(FinalItem(emptySelectItem, startDate))
                        liveTime = liveTime.plusHours(2)
                    }
                }
                "hotel" ->  {
                    Log.d(TAG, "liveTime - $liveTime")
                    Log.d(TAG, "hotelDestination - $hotelDestination")
                    if(hotelDestination.isNotEmpty()) {
                        var minLocation = RouteResult(null, 10000000)
                        var nextLocation = RouteResult(null, null)

                        for(destination in hotelDestination) {
                            runBlocking {
                                nextLocation = getResultSearch(origin, destination)
                            }

                            if (minLocation.duration!! > nextLocation.duration!!) {
                                minLocation = nextLocation
                            }
                        }

                        Log.d(TAG, "minLocation - $minLocation")
                        hotelDestination.map {
                            if(it.name == minLocation.key) {
                                origin = Origin(it.name, it.x, it.y)
                            }
                        }
                        addFinalRoute(minLocation, hotelDestination, hotelList, startDate)
                        hotelList.map {
                            if(it.title == minLocation.key) {
                                hotelDestination.remove(Destination(it.title, it.mapX?.toDouble(), it.mapY?.toDouble()))
                            }
                        }

                    }
                    else {
                        Log.d(TAG, "빈 장소 추가")
                        val emptySelectItem = SelectItem(null, "장소 추가", 32,null, null, liveTime)
                        finalRoute.add(FinalItem(emptySelectItem, startDate))
                        liveTime = liveTime.plusHours(2)
                    }
                    /* 호텔을 추가하고 난 다음에는 다음날로 넘어가고 liveTime 을 8시로 설정한다. */
                    liveTime = LocalTime.of(8, 0, 0)
                    if (startDate != endDate) {
                        startDate = (startDate?.toInt()!! + 1).toString()
                    }
                }
            }
        }

        val dayCount = endDate?.toInt()!! - origindate?.toInt()!!
        for(i in 0 ..dayCount) {
            val date = "${origindate.toInt() + i}"
            val dayRoute = finalRoute.filter { it.date == date }
            if(dayRoute.isNotEmpty()) {
                grandFinalRoute.add(GrandFinalItem(dayRoute, date))
            }
            else {
                val emptySelectItem = SelectItem(null, "장소 추가", 39,null, null, LocalTime.of(8, 0, 0))
                val emptyFinalItem = ArrayList<FinalItem>()
                emptyFinalItem.add(FinalItem(emptySelectItem, date))
                grandFinalRoute.add(GrandFinalItem(emptyFinalItem, date))
            }
//            LoadingDialog(mainActivity).dismiss()
            setMapView(mapView, finalRoute, startDate)
            finalRecyclerAdapter.notifyDataSetChanged()
        }



        finalRecyclerAdapter.setItemClickListener(object : DispositionFragment6.RVitemClickListner {
            override fun onChildItemClick(parentPosition: Int, childPosition: Int, item: List<FinalItem>) {
                // recyclerview item 클릭시 장소의 좌표로 mapView 이동
                if(item[childPosition].selectItem?.title != "장소 추가") {
                    val mapX = item[childPosition].selectItem?.mapY!!.toDouble()
                    val mapY = item[childPosition].selectItem?.mapX!!.toDouble()
                    val mapPoint = MapPoint.mapPointWithGeoCoord(mapX, mapY)
                    mapView.setMapCenterPoint(mapPoint, true)
                    mapView.setZoomLevel(3, true)
                }
                else {
                    // 만약 item 의 title 이 "장소 추가" 라면 이전 장소를 기준으로 장소 추가를 위한 LocationBasedFragment 로 이동
                    if(childPosition != 0) {
                        val formerMapX = item[childPosition - 1].selectItem?.mapY!!.toString()
                        val formerMapY = item[childPosition - 1].selectItem?.mapX!!.toString()
                        val title = item[childPosition - 1].selectItem?.title.toString()
                        val type = item[childPosition].selectItem?.type.toString()
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("장소를 추가 하시겠습니까?")
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    setFragmentResult("tourType", bundleOf("tourTypebundleKey" to type))
                                    setFragmentResult("tourTitle", bundleOf("tourTitlebundleKey" to title))
                                    setFragmentResult("tourX", bundleOf("tourXbundleKey" to formerMapX))
                                    setFragmentResult("tourY", bundleOf("tourYbundleKey" to formerMapY))
                                    binding.KakaoMapView.removeView(mapView)
                                    mainActivity.addFragment(DispositionFragment6(), LocationBasedFragment())
                                })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, id ->
                                })
                        // 다이얼로그를 띄워주기
                        builder.show()
                        viewModel.addList.observe(viewLifecycleOwner, Observer {
                            item[childPosition].selectItem = it
                            Log.d(TAG, "finalRoute - $finalRoute")
                        })

                    }
                }
            }

            // recyclerview item 길게 클릭시 진동과 함께 list 수정 가능
            override fun onChildItemLongClick(parentPosition: Int, childPosition: Int, item: List<FinalItem>) {
            }
        })


    }


    object RetrofitClient {
        val retrofitRoute : RetrofitRoute by lazy {
            Retrofit.Builder()
                .baseUrl( "https://apis-navi.kakaomobility.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitRoute::class.java)
        }
    }

    sealed class Result<out T: Any> {
        data class Success<out T : Any>(val data: T) : Result<T>()
        data class Error(val exception: String) : Result<Nothing>()
    }

    private suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val myResp = call.invoke()

            if (myResp.isSuccessful) {
                Result.Success(myResp.body()!!)
            } else {
                Result.Error(myResp.message() ?: "Something goes wrong")
            }

        } catch (e: Exception) {
            Result.Error(e.message ?: "Internet error runs")
        }
    }

    // Origin 에 가장 가까운 Destination Return
    private fun getResultSearch(origin: Origin, destination: Destination) : RouteResult {
        val originstring = "${origin.x},${origin.y},name=${origin.name}"
        val destinationstring = "${destination.x},${destination.y},name=${destination.name}"
        var nextLocation = RouteResult(null, null)
        runBlocking {
            GlobalScope.launch {
                // Result가 성공이냐 실패냐에 따라 동작처리
                when (val result = safeApiCall {
                    RetrofitClient.retrofitRoute.getKakaoRoute(KakaoApi.API_KEY,
                        originstring,
                        destinationstring,
                        "TIME",
                        true)
                }) {
                    is Result.Success -> {
                        nextLocation = addItems(result.data)
                    }
                    is Result.Error -> {
                    }
                }
            }.join()
        }
        return nextLocation
    }


    private fun addItems(searchResult: KakaoRoute?) : RouteResult {
        val nextLocation = RouteResult(null, null)
        if (!searchResult?.routes.isNullOrEmpty()) {
            // Search results available
            for (route in searchResult!!.routes) {
                // 우리는 Origin 에서 가장 가까운 지역만 알면 되기 때문에 첫번째 값만 가진다.
                if (route.result_msg == "길찾기 성공") {
                    nextLocation.key = route.summary.destination.name
                    nextLocation.duration = route.summary.duration
                }
                else Log.d(TAG, "길찾기 실패")
            }
        }
        return nextLocation
    }

    private fun addFinalRoute(nextLocation: RouteResult, destinations: ArrayList<Destination>, list: List<SelectItem>?, date: String?) {
        destinations.map { x ->
            if( x.name == nextLocation.key ){
                list?.map { y ->
                    if(y.title == x.name) {
                        liveTime = liveTime.plusSeconds(nextLocation.duration!!.toLong())
                        y.liveTime = liveTime
                        finalRoute.add(FinalItem(y, date))
                        Log.d(TAG, "finalRoute - $finalRoute")
                        liveTime = liveTime.plusMinutes(90)
                    }
                }
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



    private fun setMapView(mapView : MapView, finalRoute: ArrayList<FinalItem>, date: String?) {
        val mapPolyline = MapPolyline()
        val mapMarker = MapPOIItem()
        mapView.removeAllPOIItems()
        mapView.removeAllPolylines()
        mapPolyline.tag = 1000
        mapPolyline.lineColor = Color.argb(100, 87, 170, 209)
        finalRoute.filter { it.date == date }.map {
            if(it.selectItem?.title != "장소 추가") {
                val mapName = it.selectItem?.title.toString()
                val mapPoint = MapPoint.mapPointWithGeoCoord(it.selectItem?.mapY?.toDouble()!!, it.selectItem!!.mapX?.toDouble()!!)
                mapMarker.itemName = mapName
                mapMarker.mapPoint = mapPoint
                mapMarker.markerType = MapPOIItem.MarkerType.BluePin
                mapMarker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                mapView.addPOIItem(mapMarker)
                mapPolyline.addPoint(mapPoint)
            }
        }
        mapView.addPolyline(mapPolyline)
        val mapPointBounds = MapPointBounds(mapPolyline.mapPoints)
        val padding = 100 // px

        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))
    }

    interface RVitemClickListner {
        fun onChildItemClick(parentPosition: Int, childPosition: Int, item: List<FinalItem>)
        fun onChildItemLongClick(parentPosition: Int, childPosition: Int, item: List<FinalItem>)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden) {
        }
        else {
            setMapView(mapView, finalRoute, start)
        }
    }


}