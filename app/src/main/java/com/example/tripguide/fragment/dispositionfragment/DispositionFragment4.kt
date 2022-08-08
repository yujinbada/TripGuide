package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.model.Tour
import com.example.tripguide.recyclerview.TourAdapter
import com.example.tripguide.utils.Constants
import com.example.tripguide.utils.Constants.TAG
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_disposition4.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL
import java.net.URLEncoder

class DispositionFragment4 : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    val mobile_os = "AND"
    val mobile_app = "TripGuide"
    var contentTypeId = 12
    val type = "json"
    val serviceUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword"
    val serviceKey = "LUjHE2JtNIM0j7H1yjIJnSkVhIS6p6I6R0y5F235iEiBQL9it8MXwm6mjNUFYGbnDpVFsqLgeYnIqcMNF83ilg%3D%3D"

    private var arrayList = ArrayList<Tour>()
    private val tourAdapter = TourAdapter(arrayList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disposition4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("hintrequestKey") { key, bundle ->
            val hint = bundle.getString("hintbundleKey")
            textField_region.hint = hint
        }

        setFragmentResultListener("typerequestKey") { key, bundle ->
//            contentTypeId = bundle.getInt("typebundleKey")
        }

        region_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        region_recycler_view.adapter = tourAdapter

        textInputEditText_region.setOnKeyListener{ v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d(Constants.TAG, "여행지 검색")
                keywordParser()
                Log.d(TAG, "검색완료")

            }

            false
        }
        tourAdapter.setItemClickListener(object : TourAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Log.d(TAG, "TourAdapter - 아이템클릭 이벤트 발생")
                chip_group.addView(Chip(activity).apply {
                    text = arrayList[position].title // text 세팅
                    isCloseIconVisible = true // x 버튼 보이게 하기
                    setOnCloseIconClickListener { chip_group.removeView(this) } // x버튼 눌렀을 때 삭제되기
                })
            }

        })


    }

    // xml 파싱하기
    fun fetchXML(url : String) {
        lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수

        // xml 데이터 가져와서 파싱하기
        // 외부에서 데이터 가져올 때 화면 계속 동작하도록 AsyncTask 이용
        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            // url 이용해서 xml 읽어오기
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
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                var tagImage = false   // 이미지 태그
                var tagTitle = false   // 제목 태그
                var tagAddr1 = false   // 주소 태그
                var tagCount = false   // 조회수 태그

                var firstimage = ""    // 이미지
                var title = ""         // 제목
                var addr1 = ""         // 주소
                var readcount = ""     // 조회수

                var factory = XmlPullParserFactory.newInstance()    // 파서 생성
                factory.setNamespaceAware(true)                     // 파서 설정
                var xpp = factory.newPullParser()                   // XML 파서

                // 파싱하기
                xpp.setInput(StringReader(page))

                // 파싱 진행
                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {}
                    else if (eventType == XmlPullParser.START_TAG) {
                        var tagName = xpp.name
                        if (tagName.equals("firstimage")) tagImage = true
                        else if (tagName.equals("title")) tagTitle = true
                        else if (tagName.equals("addr1")) tagAddr1 = true
                        else if (tagName.equals("readcount")) tagCount = true
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagImage) {         // 이미지
                            firstimage = xpp.text
                            tagImage = false
                        }
                        else if (tagTitle) {    // 제목
                            title = xpp.text
                            tagTitle = false

                            // 기관명까지 다 읽으면 하나의 데이터 다 읽은 것임
                            var item = Tour(firstimage, title, addr1, readcount)
                            arrayList.add(item)
                            tourAdapter.notifyDataSetChanged()
                        }
                        else if (tagAddr1) {    // 주소
                            addr1 = xpp.text
                            tagAddr1 = false
                        }
                        else if (tagCount) {
                            readcount = xpp.text
                            tagCount = false
                        }
                    }
                    if (eventType == XmlPullParser.END_TAG) {}
                    eventType = xpp.next()
                }
                // 리사이클러 뷰에 데이터 연결
                arrayList.sortBy { it.readcount }


            }
        }

        getDangerGrade().execute()
    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    fun keywordParser() {
        Log.d(TAG, "장소 검색중")
        val textfield = textInputEditText_region.text.toString()
        val keyword = URLEncoder.encode(textfield)
        // 이 url 주소 가지고 xml에서 데이터 파싱하기
        val requstUrl = serviceUrl +
                "?serviceKey=" + serviceKey +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os +
                "&contentTypeId=" + contentTypeId +
                "&keyword=" + keyword

        fetchXML(requstUrl)
    }
}