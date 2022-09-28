package com.example.tripguide.fragment.recommend

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.adapter.RecommendAdapter
import com.example.tripguide.adapter.RecommendRecyclerAdapter
import com.example.tripguide.databinding.FragmentDisposition4Binding
import com.example.tripguide.databinding.FragmentRecommend1Binding
import com.example.tripguide.model.RecommendItem
import com.example.tripguide.model.Tour
import com.example.tripguide.utils.Constants.TAG
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL
import java.net.URLEncoder

class RecommendFragment1 : Fragment(), View.OnClickListener {
    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    val mobile_os = "AND"
    val mobile_app = "TripGuide"
    var contentTypeId = 12
    val type = "json"
    val arrange = "B"
    val areaCode = ""
    val sigunguCode = ""
    val serviceUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList"
    val serviceKey = "LUjHE2JtNIM0j7H1yjIJnSkVhIS6p6I6R0y5F235iEiBQL9it8MXwm6mjNUFYGbnDpVFsqLgeYnIqcMNF83ilg%3D%3D"

    private var mBinding: FragmentRecommend1Binding? = null
    private val binding get() = mBinding!!

    private var arrayList = ArrayList<RecommendItem>()
    private val recommendRecyclerAdapter = RecommendRecyclerAdapter(arrayList)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "RecommendFragment1 - onCreateView() called")
        // Inflate the layout for this fragment
        mBinding = FragmentRecommend1Binding.inflate(inflater, container, false)
        return binding.root
    }

    fun newInstant() : RecommendFragment1
    {
        val args = Bundle()
        val frag = RecommendFragment1()
        frag.arguments = args
        return frag
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvrecommend1.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,
            false)
        binding.rvrecommend1.adapter = recommendRecyclerAdapter

        // recycler item click event
        recommendRecyclerAdapter.setItemClickListener(object : RecommendRecyclerAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
            }

        })
    }
    // xml 파싱하기
    fun fetchXML(url: String) {
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
                var tagConTentId = false

                var firstimage = ""    // 이미지
                var title = ""         // 제목
                var contentid = ""
                var overview = ""

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
                        else if (tagName.equals("contentid")) tagConTentId = true
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
                            var item = RecommendItem(firstimage, title, overview)
                            arrayList.add(item)
                            recommendRecyclerAdapter.notifyDataSetChanged()
                        }
                        else if (tagConTentId) {    // 콘텐츠 아이디
                            contentid = xpp.text
                            overview = getOverView(contentid).toString()
                            tagConTentId = false
                        }
                    }
                    if (eventType == XmlPullParser.END_TAG) {}
                    eventType = xpp.next()
                }
            }
        }

        getDangerGrade().execute()
    }

    fun keywordParser() {
        Log.d(TAG, "장소 검색중")
        // 이 url 주소 가지고 xml에서 데이터 파싱하기
        val requstUrl = serviceUrl +
                "?serviceKey=" + serviceKey +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os +
                "&arrange=" + arrange +
                "&contentTypeId=" + contentTypeId +
                "&areaCode=" + areaCode +
                "&sigunguCode=" + sigunguCode

        fetchXML(requstUrl)
    }

    fun getOverView(contentId : String?): String {
        val serviceUrl2 = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon"
        val requstUrl2 = serviceUrl2 +
                "?serviceKey=" + serviceKey +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os +
                "&arrange=" + arrange +
                "&contentId=" + contentId

        lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수
        var tagOverView= false
        var overview = ""

        // xml 데이터 가져와서 파싱하기
        // 외부에서 데이터 가져올 때 화면 계속 동작하도록 AsyncTask 이용
        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            // url 이용해서 xml 읽어오기
            override fun doInBackground(vararg p0: Void?): Void? {
                // 데이터 스트림 형태로 가져오기
                val stream = URL(requstUrl2).openStream()
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
                        if (tagName.equals("contentid")) tagOverView = true
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagOverView) {         // 이미지
                            overview = xpp.text
                            tagOverView = false
                        }
                    }
                    if (eventType == XmlPullParser.END_TAG) {}
                    eventType = xpp.next()
                }
            }
        }

        getDangerGrade().execute()

        return overview
    }

    override fun onClick(p0: View?) {
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}
