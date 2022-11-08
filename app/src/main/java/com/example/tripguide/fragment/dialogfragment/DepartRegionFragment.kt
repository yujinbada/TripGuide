/*
 Copyright (c) Jang_Story
 https://jangstory.tistory.com/m/44
 This fragment is a fragment that receives the correct region name
 when you enter a search term using the Kakao api.
 We show the list through the recycler view when it is searched
 and also set an event when it is clicked.
*/

package com.example.tripguide.fragment.dialogfragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.databinding.FragmentDepartRegionBinding
import com.example.tripguide.model.kakao.KakaoData
import com.example.tripguide.model.MyModel
import com.example.tripguide.adapter.MyRecyclerAdapter
import com.example.tripguide.fragment.dispositionfragment.DispositionFragment
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.model.Station
import com.example.tripguide.model.kakao.kakaokeyword
import com.example.tripguide.model.kakaoroute.Destination

import com.example.tripguide.retrofit.RetrofitInterface
import com.example.tripguide.retrofit.RetrofitKeyword
import com.example.tripguide.utils.Constants
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi
import okhttp3.internal.notify
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL


class DepartRegionFragment : DialogFragment(), View.OnClickListener {
    private lateinit var callback: OnBackPressedCallback

    // To get the main activity's change fragment function
    // And since this fragment has not set the backstack, so we set the situation when back is clicked
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.changeFragment(3)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    val bundle = Bundle()
    var keyword = ""

    private val modelList = ArrayList<MyModel>() // Array to hold data
    private val myRecyclerAdapter = MyRecyclerAdapter(modelList)
    private lateinit var viewModel: SelectViewModel

    private var mBinding: FragmentDepartRegionBinding? = null
    private val binding get() = mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentDepartRegionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            SelectViewModel::class.java)

        // Set the recycler view direction, etc.
        binding.departrecyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.departrecyclerview.adapter = myRecyclerAdapter

        binding.tripName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG, "DepartRegionFragment - search event occurs")
                keyword = binding.tripName.text.toString()
                getResultSearch(keyword)
                modelList.clear()
            }
        })

        // Recycler Item click event
        myRecyclerAdapter.setItemClickListener(object : MyRecyclerAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                Log.d(TAG, "MyRecyclerAdapter - item click event occur")
                Log.d(TAG, "region_1depth_txt :${modelList[position].firstregion} region_2depth_txt: ${modelList[position].secondregion}")

                // This code is the code for where to put the text of the origin or destination in the dispositionfragment
                // when two search terms come up like Gyeongju-si, Gyeongbuk when searching for a region name.
                if (modelList[position].secondregion.toString() == ""){
                    Log.d(TAG, "departresult <- region_1depth_txt")
                    val result = modelList[position].firstregion
                    areaCode(result.toString())
                    val request = result?.removeSuffix("광역시")?.removeSuffix("특별자치시")?.removeSuffix("특별자치도")
                    getCitySearch(result + "시청")
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }
                else {
                    Log.d(TAG, "departresult <- region_2depth_txt")
                    val result = modelList[position].secondregion
                    val request = modelList[position].firstregion + " " + modelList[position].secondregion
                    areaCode(request)
                    getCitySearch(request + "청")
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }
                dismissAllowingStateLoss()
            }
        })
    }

    override fun onClick(v: View?) {
    }


    private fun getResultSearch(keyword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(KakaoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RetrofitInterface::class.java)
        val call = api.getKakaoAddress(KakaoApi.API_KEY, keyword)

        call.enqueue(object : Callback<KakaoData> {
            override fun onResponse(call: Call<KakaoData>, response: Response<KakaoData>) {
                Log.d(TAG, "communication success")
                addItems(response.body())
            }

            override fun onFailure(call: Call<KakaoData>, t: Throwable) {
                Log.d(TAG, "error : " + t.message)
            }
        })
    }


    private fun addItems(searchResult: KakaoData?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            // Search results available
            for (document in searchResult!!.documents) {
                Log.d(TAG, "DepartRegionFragment - addItems() called")
                val item = MyModel(document.address.region_1depth_name,
                    document.address.region_2depth_name)
                modelList.add(item)
                myRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }
    var areacode = ""
    fun areaCode(name: String) {
        val areaNameSplit = name.split(" ")
        Log.d(TAG, "areaNameSplit - $areaNameSplit")
        when(areaNameSplit[0].removeSuffix("광역시")) {
            "서울" -> areacode = "1"
            "인천" -> areacode = "2"
            "대전" -> areacode = "3"
            "대구" -> areacode = "4"
            "광주" -> areacode = "5"
            "부산" -> areacode = "6"
            "울산" -> areacode = "7"
            "세종특별자치시" -> areacode = "8"
            "경기" -> areacode = "31"
            "강원" -> areacode = "32"
            "충북" -> areacode = "33"
            "충남" -> areacode = "34"
            "경북" -> areacode = "35"
            "경남" -> areacode = "36"
            "전북" -> areacode = "37"
            "전남" -> areacode = "38"
            else -> areacode = "39"
        }
        Log.d(TAG, "areaCode - $areacode")
        viewModel.areaCode.value = areacode

        if (!(areacode.toInt() in 1..8 || areacode.toInt() == 39)) {
            val sigunguName = areaNameSplit[1].removeSuffix("시").removeSuffix("군")
            findSiGunGuCode(areacode, sigunguName)
        }
        else viewModel.sigunguCode.value = ""
    }



    fun findSiGunGuCode(areaCode : String, sigunguName : String) {
        val mobile_os = "AND"
        val mobile_app = "TripGuide"
        val serviceUrl = "http://apis.data.go.kr/B551011/KorService/areaCode"
        val serviceKey = "LUjHE2JtNIM0j7H1yjIJnSkVhIS6p6I6R0y5F235iEiBQL9it8MXwm6mjNUFYGbnDpVFsqLgeYnIqcMNF83ilg%3D%3D"

        val requstUrl = serviceUrl +
                "?serviceKey=" + serviceKey +
                "&areaCode=" + areaCode +
                "&numOfRows=30" +
                "&pageNo=1" +
                "&MobileOS=" + mobile_os +
                "&MobileApp=" + mobile_app

        fetchXML(requstUrl, sigunguName)
    }



    private fun fetchXML(url: String, sigunguName: String) {
        lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수
        // xml 데이터 가져와서 파싱하기
        // 외부에서 데이터 가져올 때 화면 계속 동작하도록 AsyncTask 이용
        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            // url 이용해서 xml 읽어오기
            override fun doInBackground(vararg p0: Void?): Void? {
                // 데이터 스트림 형태로 가져오기
                var stream = URL(url).openStream()
                var bufReader = BufferedReader(InputStreamReader(stream, "UTF-8"))

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

                var tagCode = false    // code tag
                var tagAreaName = false    // name tag

                var code = ""          // code
                var areaname = ""          // name


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
                        if (tagName.equals("code")) tagCode = true
                        else if (tagName.equals("name")) tagAreaName = true

                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagAreaName) {
                            areaname = xpp.text
                            if(areaname.contains(sigunguName)) {
                                Log.d(TAG, "sigunguname - $areaname sigungucode - $code")
                                viewModel.sigunguCode.value = code
                            }
                            tagAreaName = false
                        }
                        else if (tagCode) {
                            code = xpp.text
                            tagCode = false
                        }
                    }
                    if (eventType == XmlPullParser.END_TAG) {}
                    eventType = xpp.next()
                }
            }
        }
        getDangerGrade().execute()
    }

    private fun getCitySearch(keyword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(KakaoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RetrofitKeyword::class.java)
        val call = api.getKakaoKeyword(KakaoApi.API_KEY, keyword)

        call.enqueue(object : Callback<kakaokeyword> {
            override fun onResponse(call: Call<kakaokeyword>, response: Response<kakaokeyword>) {
                Log.d(Constants.TAG, "communication success")
                for (document in response.body()!!.documents) {
                    val item = Destination(document.place_name,
                        document.x.toDouble(),
                        document.y.toDouble())
                    if(item.name?.substring(item.name?.length!! - 1) == "청") {
                        Log.d(TAG, "arriveOffice - $item")
                        viewModel.setArriveOfficeRegion(item)
                        break
                    }
                    else continue
                }
            }

            override fun onFailure(call: Call<kakaokeyword>, t: Throwable) {
                Log.d(Constants.TAG, "error : " + t.message)
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}