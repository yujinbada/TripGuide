package fragment.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.R
import com.example.tripguide.kakao.KakaoData
import com.example.tripguide.model.MyModel
import com.example.tripguide.recyclerview.MyRecyclerAdapter
import com.example.tripguide.retrofit.RetrofitInterface
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi
import kotlinx.android.synthetic.main.fragment_depart_region.*
import kotlinx.android.synthetic.main.layout_recycler_item.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DepartRegionFragment : Fragment() {
    // 데이터를 담을 그릇 즉 배열
    var modelList = ArrayList<MyModel>()
    private var myRecyclerAdapter = MyRecyclerAdapter(modelList)
    private var keyword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_depart_region, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 리사이클러뷰 설정
        my_recycler_view.apply {
            // 리사이클러뷰 방향 등 설정
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = myRecyclerAdapter
        }
        myRecyclerAdapter.setItemClickListener(object : MyRecyclerAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                region_txt.text = modelList[position].region
            }
        })
        region_search_btn.setOnClickListener {
            keyword = region_txt.text.toString()
            getResultSearch(keyword)
        }

    }


    fun getResultSearch(address: String) {
        val kakao = MutableLiveData<KakaoData>()

        val retrofit = Retrofit.Builder()
            .baseUrl(KakaoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val kakaoApi = retrofit.create(RetrofitInterface::class.java)

        val call: Call<KakaoData> = kakaoApi.getKakaoAddress(KakaoApi.API_KEY, keyword)

        call.enqueue(object : retrofit2.Callback<KakaoData> {
            override fun onResponse(call: Call<KakaoData>, response: Response<KakaoData>) {
                addItems(response.body())
            }

            override fun onFailure(call: Call<KakaoData>, t: Throwable) {
                Log.d(TAG, "에러 : " + t.message)
            }
        })
    }

    fun addItems(searchResult: KakaoData?) {
        if (!searchResult?.addresses.isNullOrEmpty()) {
            //검색결과있음
            modelList.clear()
            for (address in searchResult!!.addresses) {
                val item = MyModel(address.region_1depth_name)
                modelList.add((item))
            }
        } else
            Toast.makeText(activity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
    }
}