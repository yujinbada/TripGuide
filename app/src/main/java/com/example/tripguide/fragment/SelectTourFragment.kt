package com.example.tripguide.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.databinding.FragmentSelectTourBinding
import com.example.tripguide.fragment.recommend.RecommendFragment1
import com.example.tripguide.model.RecommendItem
import com.example.tripguide.model.SelectItem
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.utils.Constants.TAG
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.lang.Math.round
import java.net.URL
import java.text.DecimalFormat
import kotlin.math.roundToInt


class SelectTourFragment : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var mBinding: FragmentSelectTourBinding? = null
    private val binding get() = mBinding!!
    private var imgList = ArrayList<SlideModel>()
    private lateinit var viewModel: SelectViewModel

    val mobile_os = "AND"
    val mobile_app = "TripGuide"
    var contentId = ""
    val serviceUrl = "http://apis.data.go.kr/B551011/KorService/detailImage"
    val serviceKey = "LUjHE2JtNIM0j7H1yjIJnSkVhIS6p6I6R0y5F235iEiBQL9it8MXwm6mjNUFYGbnDpVFsqLgeYnIqcMNF83ilg%3D%3D"

    private var title = ""
    private var image = ""

    var tourX = 37.53737528
    var tourY = 127.00557633

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
            mBinding = FragmentSelectTourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(SelectViewModel::class.java)

        Log.d(TAG, "SelectTourFragment - onViewCreated() called")
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
            Log.d(TAG, "tourX - $tourX")

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
        binding.add.setOnClickListener(this)
    }

    private fun imgParser(contentId : String) {
        val requstUrl = serviceUrl +
                "?serviceKey=" + serviceKey +
                "&numOfRows=30" +
                "&pageNo=1" +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os +
                "&contentId=" + contentId +
                "&imageYN=Y" +
                "&subImageYN=Y"

        fetchXML(requstUrl)
    }

    // xml 파싱하기
    private fun fetchXML(url: String) {
        lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수

        // xml 데이터 가져와서 파싱하기
        // 외부에서 데이터 가져올 때 화면 계속 동작하도록 AsyncTask 이용
        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            // url 이용해서 xml 읽어오기
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg p0: Void?): Void? {
                // 데이터 스트림 형태로 가져오기
                val stream = URL(url).openStream()
                val bufReader = BufferedReader(InputStreamReader(stream, "UTF-8"))

                // 한줄씩 읽어서 스트링 형태로 바꾼 후 page에 저장
                page = ""
                var line = bufReader.readLine()
                while (line != null) {
                    page += line
                    line = bufReader.readLine()
                }

                return null
            }

            // 읽어온 xml 파싱하기
            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                var tagOriginimgurl	 = false   // 이미지 태그
                var tagSmallimageurl = false

                var originimgurl = ""
                var smallimageurl = "" // 이미지

                val factory = XmlPullParserFactory.newInstance()    // 파서 생성
                factory.setNamespaceAware(true)                     // 파서 설정
                val xpp = factory.newPullParser()                   // XML 파서

                // 파싱하기
                xpp.setInput(StringReader(page))

                // 파싱 진행
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
                        if (tagOriginimgurl) {         // 이미지
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
                "&contentTypeId=12"

        fetchXML2(requstUrl)
    }

    // xml 파싱하기
    private fun fetchXML2(url: String) {
        lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수

        // xml 데이터 가져와서 파싱하기
        // 외부에서 데이터 가져올 때 화면 계속 동작하도록 AsyncTask 이용
        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            // url 이용해서 xml 읽어오기
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg p0: Void?): Void? {
                // 데이터 스트림 형태로 가져오기
                val stream = URL(url).openStream()
                val bufReader = BufferedReader(InputStreamReader(stream, "UTF-8"))

                // 한줄씩 읽어서 스트링 형태로 바꾼 후 page에 저장
                page = ""
                var line = bufReader.readLine()
                while (line != null) {
                    page += line
                    line = bufReader.readLine()
                }

                return null
            }

            // 읽어온 xml 파싱하기
            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                var tagUseseason = false
                var tagUsetime = false

                var useseason = ""
                var usetime = ""

                var factory = XmlPullParserFactory.newInstance()    // 파서 생성
                factory.setNamespaceAware(true)                     // 파서 설정
                var xpp = factory.newPullParser()                   // XML 파서

                // 파싱하기
                xpp.setInput(StringReader(page))

                // 파싱 진행
                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType != XmlPullParser.START_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            var tagName = xpp.name
                            if (tagName.equals("useseason")) tagUseseason = true
                            else if (tagName.equals("usetime")) tagUsetime = true
                        }
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagUseseason) {         // 이미지
                            useseason = xpp.text.replace("<br />", "\n").replace("<br/>", "\n")
                            tagUseseason = false
                            if(useseason != ""){
                                binding.seletedUseseason.text = "이용시기 : $useseason"
                            }
                            else {
                                binding.seletedUseseason.text = "이용시기 : 항시 이용가능"
                            }

                        }
                        if(tagUsetime) {
                            usetime = xpp.text.replace("<br />", "\n").replace("<br/>", "\n")
                            tagUsetime = false
                            if(usetime != null) {
                                binding.selectedUsetime.text = "이용시간 : $usetime"
                            }
                            else binding.selectedUsetime.text = "이용시간 : 항시 이용가능"

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
                mainActivity.removeFragment(RecommendFragment1())
            }
            R.id.add -> {
                viewModel.addTask(SelectItem(image, title, 12, tourX.toString(), tourY.toString(), 0))
                Toast.makeText(activity, "장소가 추가 되었습니다!", Toast.LENGTH_SHORT).show()
                mainActivity.removeFragment(RecommendFragment1())
            }
        }
    }

}