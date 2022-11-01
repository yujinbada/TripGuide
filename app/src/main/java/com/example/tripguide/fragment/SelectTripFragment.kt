package com.example.tripguide.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.databinding.FragmentSelectFestivalBinding
import com.example.tripguide.databinding.FragmentSelectTripBinding
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.utils.Constants
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL

@Suppress("DEPRECATION")
class SelectTripFragment : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var mBinding: FragmentSelectTripBinding? = null
    private val binding get() = mBinding!!
    private var imgList = ArrayList<SlideModel>()
    private lateinit var viewModel: SelectViewModel

    val mobile_os = "AND"
    val mobile_app = "TripGuide"
    var contentId = ""
    val serviceUrl = "http://apis.data.go.kr/B551011/KorService/detailImage"
    val serviceKey = "Y1V3I9PnBtKBrgRfQtGKLGEPGcrpDSglmZXIee2CwJXoFWrbeHLzyU979rwuDaCfXBUjgYxoA2xZeV%2BRdOQ5mQ%3D%3D"

    private var title = ""
    private var image = ""

    var tourX = 37.53737528
    var tourY = 127.00557633

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentSelectTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            SelectViewModel::class.java)

        Log.d(Constants.TAG, "SelectTripFragment - onViewCreated() called")
        setFragmentResultListener("tourIdrequestKey") { key, bundle ->
            val result = bundle.getString("tourIdbundleKey").toString()
            contentId = result
            imgParser(contentId)
            introParser(contentId)
        }
        setFragmentResultListener("tourTitle") { key, bundle ->
            val result = bundle.getString("tourTitlebundleKey").toString()
            binding.selectedTitle.text = result
            title = result
        }
        setFragmentResultListener("tourImage") { key, bundle ->
            val result = bundle.getString("tourImagebundleKey").toString()
            image = result
        }
        setFragmentResultListener("tourOverView") { key, bundle ->
            val result = bundle.getString("tourOverViewdbundleKey").toString()
            binding.selectedOverView.text = result
        }
        setFragmentResultListener("tourX") { key, bundle ->
            val result = bundle.getString("tourXbundleKey").toString()
            tourX = result.toDouble()
            Log.d(Constants.TAG, "tourX - $tourX")

        }
        setFragmentResultListener("tourY") { key, bundle ->
            val result = bundle.getString("tourYbundleKey").toString()
            tourY = result.toDouble()
            val mapView = MapView(activity)
            val mapPoint = MapPoint.mapPointWithGeoCoord(tourY, tourX)
            binding.KakaoMapView.addView(mapView)
            mapView.setMapCenterPoint(mapPoint, true)
            mapView.setZoomLevel(5, true)

            //마커 생성
            val marker = MapPOIItem()
            marker.itemName = "이곳이 $title 입니다"
            marker.mapPoint = mapPoint
            marker.markerType = MapPOIItem.MarkerType.BluePin
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

            mapView.addPOIItem(marker)
        }

        binding.back.setOnClickListener(this)
    }

    private fun imgParser(contentId : String) {
        val requstUrl = serviceUrl +
                "?serviceKey=" + serviceKey +
                "&numOfRows=30" +
                "&pageNo=1" +
                "&MobileOS=" + mobile_os +
                "&MobileApp=" + mobile_app +
                "&contentId=" + contentId +
                "&imageYN=Y" +
                "&subImageYN=Y"

        fetchXML(requstUrl)
    }

    private fun fetchXML(url: String) {
        lateinit var page : String

        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg p0: Void?): Void? {
                val stream = URL(url).openStream()
                val bufReader = BufferedReader(InputStreamReader(stream, "UTF-8"))

                page = ""
                var line = bufReader.readLine()
                while (line != null) {
                    page += line
                    line = bufReader.readLine()
                }

                return null
            }

            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                var tagOriginimgurl	 = false
                var tagSmallimageurl = false

                var originimgurl = ""
                var smallimageurl = ""

                val factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(true)
                val xpp = factory.newPullParser()

                xpp.setInput(StringReader(page))

                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType != XmlPullParser.START_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            val tagName = xpp.name
                            if (tagName.equals("originimgurl")) tagOriginimgurl = true
                            else if (tagName.equals("smallimageurl")) tagSmallimageurl = true
                        }
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagOriginimgurl) {
                            originimgurl = xpp.text
                            tagOriginimgurl = false
                            imgList.add(SlideModel(originimgurl, ScaleTypes.CENTER_CROP))
                            binding.ImageSlider.setImageList(imgList)
                        }
                        if(tagSmallimageurl) {
                            smallimageurl = xpp.text
                            tagSmallimageurl = false
                        }
                    }
                    eventType = xpp.next()
                }
            }
        }

        getDangerGrade().execute()
    }

    private fun introParser(contentId : String) {
        val serviceUrl2 = "http://apis.data.go.kr/B551011/KorService/detailIntro"
        val requstUrl = serviceUrl2 +
                "?serviceKey=" + serviceKey +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os +
                "&contentId=" + contentId +
                "&contentTypeId=32"
        fetchXML2(requstUrl)
    }

    private fun fetchXML2(url: String) {
        lateinit var page : String

        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg p0: Void?): Void? {
                val stream = URL(url).openStream()
                val bufReader = BufferedReader(InputStreamReader(stream, "UTF-8"))

                page = ""
                var line = bufReader.readLine()
                while (line != null) {
                    page += line
                    line = bufReader.readLine()
                }

                return null
            }

            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                var tagExpguide = false
                var tagUseseason = false
                var tagUsetime = false
                var tagRestdate = false
                var tagInfocenter = false

                var expguide = ""
                var useseason = ""
                var usetime = ""
                var restdate = ""
                var infocenter = ""

                var factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(true)
                var xpp = factory.newPullParser()

                xpp.setInput(StringReader(page))

                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType != XmlPullParser.START_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            var tagName = xpp.name
                            if (tagName.equals("expguide")) tagExpguide = true
                            else if (tagName.equals("useseason")) tagUseseason = true
                            else if (tagName.equals("usetime")) tagUsetime = true
                            else if (tagName.equals("restdate")) tagRestdate = true
                            else if (tagName.equals("infocenter")) tagInfocenter = true
                        }
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagExpguide) {
                            expguide = xpp.text.replace("<br />", "\n")
                            tagExpguide = false
                            if(expguide != ""){
                                binding.seletedRoomtype.text = "체험 안내 : $expguide"
                            }
                            else {
                                binding.seletedRoomtype.text = "체험 안내 : 홈페이지 공지사항 참고"
                            }

                        }
                        if(tagUseseason) {
                            useseason = xpp.text.replace("<br />", "\n")
                            tagUseseason = false
                            if(useseason != null) {
                                binding.selectedCheckintime.text = "이용 시기 : $useseason"
                            }
                            else binding.selectedCheckintime.text = "이용 시기 : 홈페이지 공지사항 참고"

                        }
                        if(tagUsetime) {
                            usetime = xpp.text.replace("<br />", "\n")
                            tagUsetime = false
                            if(usetime != null) {
                                binding.selectedCheckouttime.text = "이용 시간 : $usetime"
                            }
                            else binding.selectedCheckouttime.text = "이용 시간 : 홈페이지 공지사항 참고"

                        }
                        if(tagRestdate) {
                            restdate = xpp.text.replace("<br />", "\n")
                            tagRestdate = false
                            if(restdate != null) {
                                binding.selectedSubfacility.text = "휴무일 : $restdate"
                            }
                            else binding.selectedSubfacility.text = "휴무일 : 홈페이지 공지사항 참고"

                        }
                        if(tagInfocenter) {
                            infocenter = xpp.text.replace("<br />", "\n")
                            tagInfocenter = false
                            if(infocenter != null) {
                                binding.selectedReservationlodging.text = "문의 및 안내 : $infocenter"
                            }
                            else binding.selectedReservationlodging.text = "문의 및 안내 : 홈페이지 공지사항 참고"

                        }
                    }
                    eventType = xpp.next()
                }
            }
        }

        getDangerGrade().execute()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.back -> {
                mainActivity.removeFragment(SelectTripFragment())
            }
        }
    }

}