/*
 This fragment is a fragment that receives the correct region name
 when you enter a search term using the Kakao api.
 We show the list through the recycler view when it is searched
 and also set an event when it is clicked.
*/

package com.example.tripguide.fragment

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.databinding.FragmentDepartRegionBinding
import com.example.tripguide.kakao.KakaoData
import com.example.tripguide.model.MyModel
import com.example.tripguide.adapter.MyRecyclerAdapter
import com.example.tripguide.fragment.recommend.findAreaCode
import com.example.tripguide.model.RecommendAreaCode
import com.example.tripguide.retrofit.RetrofitInterface
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi
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


class DepartRegionFragment : Fragment(), View.OnClickListener {
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

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 48ad751ca72b3e49a7f746f46b40b142"
    }

    val bundle = Bundle()
    var keyword = ""

    private val modelList = ArrayList<MyModel>() // Array to hold data
    private val myRecyclerAdapter = MyRecyclerAdapter(modelList)


    private var mBinding: FragmentDepartRegionBinding? = null
    private val binding get() = mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentDepartRegionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        // Set the recycler view direction, etc.
        binding.departrecyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.departrecyclerview.adapter = myRecyclerAdapter

        // This code modifies the hint of DepartRegionFragment to reuse one fragment when inputting the departure and destination.
        setFragmentResultListener("hintrequestKey") { key, bundle ->
            val hint = bundle.getString("hintbundleKey")
            binding.departtextField.hint = hint
        }

        binding.tripName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG, "DepartRegionFragment - search event occurs")
                keyword = binding.tripName.text.toString()
                getResultSearch(keyword)
                if(binding.tripName.text.toString() == "") {
                    modelList.clear()

                }
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
                    findAreaCode(result.toString())
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }
                else {
                    Log.d(TAG, "departresult <- region_2depth_txt")
                    val result = modelList[position].secondregion
                    findAreaCode( modelList[position].firstregion + " " + modelList[position].secondregion)
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }

                mainActivity.changeFragment(3)
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
        }
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

    private val arrayList = ArrayList<RecommendAreaCode>()
    private val areaNameMap = mutableMapOf<String, String>()

    val mobile_os = "AND"
    val mobile_app = "TripGuide"
    val serviceUrl = "http://apis.data.go.kr/B551011/KorService/areaCode"
    val serviceKey = "LUjHE2JtNIM0j7H1yjIJnSkVhIS6p6I6R0y5F235iEiBQL9it8MXwm6mjNUFYGbnDpVFsqLgeYnIqcMNF83ilg%3D%3D"

    fun findAreaCode(areaname : String): ArrayList<RecommendAreaCode> {
        Log.d(TAG, "FindAreaCode() called")
        val requstUrl = serviceUrl +
                "?serviceKey=" + serviceKey +
                "&numOfRows=20" +
                "&MobileApp=" + mobile_app +
                "&MobileOS=" + mobile_os

        fetchXML(requstUrl, areaname)

        return arrayList
    }

    private fun fetchXML(url: String, areaname: String) {
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
                            tagAreaName = false
                            // 기관명까지 다 읽으면 하나의 데이터 다 읽은 것임
                            areaNameMap[areaname] = code

                            Log.d(TAG, "areaNameMap - $areaNameMap")
                            val areaNameSplit = areaname.split(" ")
                            if(areaNameSplit.size == 1) {

                                Log.d(TAG, "areaNameMap - $areaNameMap")
                                val areaCode = areaNameMap.filter { it.key == areaname }.values.toString()
                                arrayList.add(RecommendAreaCode(areaCode))
                            }
                            else {
                                val areaCode = areaNameMap.filter { it.key == areaNameSplit[0] }.values.toString()
                                areaNameMap.clear()

                                val requstUrl2 = serviceUrl +
                                        "?serviceKey=" + serviceKey +
                                        "&areaCode=" + areaCode +
                                        "&numOfRows=20" +
                                        "&MobileApp=" + mobile_app +
                                        "&MobileOS=" + mobile_os

                                val sigunguCode = areaNameMap.filter { it.key == areaNameSplit[1] }.values.toString()
                                arrayList.add(RecommendAreaCode(areaCode, sigunguCode))
                            }

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

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}