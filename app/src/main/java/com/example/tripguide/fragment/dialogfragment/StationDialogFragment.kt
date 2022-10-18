/*
 This StainDialogFragment searches the user's departure and arrival areas and sends them to DispositionFragment222.
*/
package com.example.tripguide.fragment.dialogfragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.adapter.StationAdapter
import com.example.tripguide.databinding.FragmentStationDialogBinding
import com.example.tripguide.kakao.KakaoData
import com.example.tripguide.model.Station
import com.example.tripguide.retrofit.RetrofitInterface
import com.example.tripguide.utils.Constants
import com.example.tripguide.utils.KakaoApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StationDialogFragment : DialogFragment(), View.OnClickListener {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var mBinding : FragmentStationDialogBinding? = null
    private val binding get() = mBinding!!

    private val stationList = ArrayList<Station>()
    private val stationAdapter = StationAdapter(stationList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentStationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 48ad751ca72b3e49a7f746f46b40b142"
    }

    val bundle = Bundle()
    var keyword = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the recycler view direction, etc.
        binding.departrecyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.departrecyclerview.adapter = stationAdapter

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
                Log.d(Constants.TAG, "DepartRegionFragment - search event occurs")
                keyword = binding.tripName.text.toString().replace(" ", "")
                getResultSearch(keyword)
                if(binding.tripName.text.toString() == "") {
                    stationList.clear()
                }
            }
        })

        // Recycler Item click event
        stationAdapter.setItemClickListener(object : StationAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val result = stationList[position].name
                setFragmentResult("stationName", bundleOf("stationNameKey" to result))
                dismiss()
            }
        })
    }

    private fun getResultSearch(keyword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(KakaoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var api = retrofit.create(RetrofitInterface::class.java)
        var call = api.getKakaoAddress(KakaoApi.API_KEY, keyword)

        call.enqueue(object : Callback<KakaoData> {
            override fun onResponse(call: Call<KakaoData>, response: Response<KakaoData>) {
                Log.d(Constants.TAG, "communication success")
                addItems(response.body())
            }

            override fun onFailure(call: Call<KakaoData>, t: Throwable) {
                Log.d(Constants.TAG, "error : " + t.message)
            }
        })
    }


    private fun addItems(searchResult: KakaoData?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            // Search results available
            for (document in searchResult!!.documents) {
                Log.d(Constants.TAG, "StationDialogFragment - addItems() called")
                val item = Station(document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble())
                stationList.add(item)
                stationAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(v: View?) {
    }

}