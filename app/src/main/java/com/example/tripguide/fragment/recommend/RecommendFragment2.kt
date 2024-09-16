package com.example.tripguide.fragment.recommend

import CustomDecoration
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.adapter.RecommendRecyclerAdapter

import com.example.tripguide.databinding.FragmentRecommend2Binding
import com.example.tripguide.fragment.SelectCafeFragment
import com.example.tripguide.fragment.SelectTourFragment
import com.example.tripguide.model.RecommendItem
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.utils.Constants
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL

class RecommendFragment2 : Fragment(), View.OnClickListener {

    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    val num_of_rows = 10
    val page_no = 1
    val mobile_os = "AND"
    val mobile_app = "TripGuide"
    val arrange = "B"
    var contentTypeId = 39
    //var totalCount = 3
    var areaCode = ""
    var sigunguCode = ""
    //var cat1 = ""
    //var modifiedtome = ""
    //var readcount = ""
    //var tel = ""

    val type = "json"

    private lateinit var viewModel: SelectViewModel
    private var mBinding: FragmentRecommend2Binding? = null
    private val binding get() = mBinding!!
    private var arrayList = ArrayList<RecommendItem>()
    private val recommendRecyclerAdapter = RecommendRecyclerAdapter(arrayList)


    val serviceUrl = "http://apis.data.go.kr/B551011/KorService/areaBasedList"
    val serviceKey = "Y1V3I9PnBtKBrgRfQtGKLGEPGcrpDSglmZXIee2CwJXoFWrbeHLzyU979rwuDaCfXBUjgYxoA2xZeV%2BRdOQ5mQ%3D%3D"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        Log.d(Constants.TAG, "RecommendFragment2 - onCreateView() called")
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            SelectViewModel::class.java)
        arrayList.clear()
        mBinding = FragmentRecommend2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keywordParser()
        binding.rvCafeList.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,
            false)
        val customDecoration = CustomDecoration(2f, 10f, Color.GRAY)
        binding.rvCafeList.addItemDecoration(customDecoration)

        binding.rvCafeList.adapter = recommendRecyclerAdapter
        binding.rvCafeList.apply {
            itemAnimator = null
        }

        recommendRecyclerAdapter.setItemClickListener(object : RecommendRecyclerAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val tourId = arrayList[position].recommendcontentId.toString()
                val tourTitle = arrayList[position].recommendTitle.toString()
                val tourImage = arrayList[position].recommendImage.toString()
                val tourOverView = arrayList[position].tourOverView.toString()
                val tourX = arrayList[position].recommendmapX.toString()
                val tourY = arrayList[position].recommendmapY.toString()

                setFragmentResult("tourIdrequestKey", bundleOf("tourIdbundleKey" to tourId))
                setFragmentResult("tourTitle", bundleOf("tourTitlebundleKey" to tourTitle))
                setFragmentResult("tourImage", bundleOf("tourImagebundleKey" to tourImage))
                setFragmentResult("tourOverView", bundleOf("tourOverViewdbundleKey" to tourOverView))
                setFragmentResult("tourX", bundleOf("tourXbundleKey" to tourX))
                setFragmentResult("tourY", bundleOf("tourYbundleKey" to tourY))
                mainActivity.addFragment(RecommendFragment2(), SelectCafeFragment())

            }

        })
    }

    fun keywordParser() {
        Log.d(Constants.TAG, "장소 검색중")
        val requstUrl = serviceUrl +
                "?serviceKey=" + serviceKey +
                "&numOfRows=10" +
                "&pageNo=1" +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os +
                "&arrange=" + arrange +
                "&contentTypeId=" + contentTypeId +
                "&areaCode=" + viewModel.areaCode.value +
                "&sigunguCode=" + viewModel.sigunguCode.value

        fetchXML(requstUrl)
    }

    private fun fetchXML(url: String) {
        lateinit var page : String

        class getDangerGrade : AsyncTask<Void, Void, Void>() {
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

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                var tagImage = false
                var tagTitle = false
                var tagConTentId = false
                var tagMapX = false
                var tagMapY = false

                var firstimage = ""
                var title = ""
                var mapx = ""
                var mapy = ""
                var contentid = "126508"

                var factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(true)
                var xpp = factory.newPullParser()

                xpp.setInput(StringReader(page))

                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType != XmlPullParser.START_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            var tagName = xpp.name
                            if (tagName.equals("firstimage")) tagImage = true
                            else if (tagName.equals("title")) tagTitle = true
                            else if (tagName.equals("mapx")) tagMapX = true
                            else if (tagName.equals("mapy")) tagMapY = true
                            else if (tagName.equals("contentid")) tagConTentId = true
                        }
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagImage) {
                            firstimage = xpp.text
                            tagImage = false
                        }
                        else if (tagTitle) {
                            title = xpp.text
                            tagTitle = false
                            val item = RecommendItem(firstimage, title, contentid,"" , mapx, mapy)
                            arrayList.add(item)
                            getOverView(contentid)
                        }
                        else if (tagMapX) {
                            mapx = xpp.text
                            tagMapX = false

                        }
                        else if (tagMapY) {
                            mapy = xpp.text
                            tagMapY = false

                        }
                        else if (tagConTentId) {
                            contentid = xpp.text
                            tagConTentId = false

                        }
                    }
                    eventType = xpp.next()
                }
            }
        }

        getDangerGrade().execute()
    }

    fun getOverView(contentId : String) {
        val serviceUrl2 = "http://apis.data.go.kr/B551011/KorService/detailCommon"
        val overviewyn = "Y"
        val requstUrl2 = serviceUrl2 +
                "?serviceKey=" + serviceKey +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os +
                "&contentId=" + contentId +
                "&overviewYN=Y"

        fetchXML2(requstUrl2)
    }


    fun fetchXML2(url: String) {
        lateinit var page : String

        var tagOverView= false
        var overView = "blank"

        class GetDangerGrade : AsyncTask<Void, Void, Void>() {
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

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                var tagConTentId = false
                var contentid = ""

                var factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(true)
                var xpp = factory.newPullParser()

                xpp.setInput(StringReader(page))

                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {}
                    else if (eventType == XmlPullParser.START_TAG) {
                        var tagName = xpp.name
                        if (tagName.equals("overview")) tagOverView = true
                        else if (tagName.equals("contentid")) tagConTentId = true
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagOverView) {
                            overView = xpp.text.replace("<br />", "\n")
                                .replace("<br/>", "\n")
                                .replace("<br>", "")
                            tagOverView = false
                            arrayList.map {
                                if( it.recommendcontentId == contentid) {
                                    it.tourOverView = overView
                                    recommendRecyclerAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                        else if (tagConTentId) {
                            contentid = xpp.text
                            tagConTentId = false

                        }
                    }
                    if (eventType == XmlPullParser.END_TAG) {}
                    eventType = xpp.next()
                }
            }
        }
        GetDangerGrade().execute()

    }

    fun newInstant() : RecommendFragment2
    {
        val args = Bundle()
        val frag = RecommendFragment2()
        frag.arguments = args
        return frag
    }

    override fun onClick(p0: View?) {
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}