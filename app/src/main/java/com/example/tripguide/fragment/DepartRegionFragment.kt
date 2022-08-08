package com.example.tripguide.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.kakao.KakaoData
import com.example.tripguide.model.MyModel
import com.example.tripguide.recyclerview.MyRecyclerAdapter
import com.example.tripguide.retrofit.RetrofitInterface
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi
import kotlinx.android.synthetic.main.fragment_depart_region.*
import kotlinx.android.synthetic.main.fragment_depart_region.tripName
import kotlinx.android.synthetic.main.layout_recycler_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HEAD


class DepartRegionFragment : Fragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 48ad751ca72b3e49a7f746f46b40b142"
    }
    val bundle = Bundle()


    // 데이터를 담을 그릇 즉 배열
    private val modelList = ArrayList<MyModel>()
    private val myRecyclerAdapter = MyRecyclerAdapter(modelList)
    private var keyword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_depart_region, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 리사이클러뷰 방향 등 설정
        depart_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        depart_recycler_view.adapter = myRecyclerAdapter

        setFragmentResultListener("hintrequestKey") { key, bundle ->
            val hint = bundle.getString("hintbundleKey")
            depart_textField.hint = hint
        }

        tripName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG, "DepartRegionFragment - 출발지 버튼 클릭")
                keyword = tripName.text.toString()
                getResultSearch(keyword)
            }
        })


        myRecyclerAdapter.setItemClickListener(object : MyRecyclerAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                Log.d(TAG, "MyRecyclerAdapter - 아이템클릭 이벤트 발생")
                Log.d(TAG, "region_1depth_txt :${modelList[position].firstregion} region_2depth_txt: ${modelList[position].secondregion}")

                if (modelList[position].secondregion.toString() == ""){
                    Log.d(TAG, "departresult에 region_1depth_txt 대입")
                    val result = region_1depth_txt?.text.toString()
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }
                else {
                    Log.d(TAG, "departresult에 region_2depth_txt 대입")
                    val result = region_2depth_txt?.text.toString()
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
                Log.d(TAG, "통신 성공")
                addItems(response.body())
            }

            override fun onFailure(call: Call<KakaoData>, t: Throwable) {
                Log.d(TAG, "에러 : " + t.message)
            }
        })
    }


    private fun addItems(searchResult: KakaoData?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            //검색결과있음
            modelList.clear()
            for (document in searchResult!!.documents) {
                Log.d(TAG, "DepartRegionFragment - addItems() called")
                val item = MyModel(document.address.region_1depth_name,
                                   document.address.region_2depth_name)
                modelList.add(item)
                myRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }
}