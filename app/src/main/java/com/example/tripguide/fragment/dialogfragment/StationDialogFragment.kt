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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripguide.MainActivity
import com.example.tripguide.adapter.StationAdapter
import com.example.tripguide.databinding.FragmentStationDialogBinding
import com.example.tripguide.model.kakao.KakaoData
import com.example.tripguide.model.kakao.kakaokeyword
import com.example.tripguide.model.SelectItem
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.model.Station
import com.example.tripguide.retrofit.RetrofitInterface
import com.example.tripguide.retrofit.RetrofitKeyword
import com.example.tripguide.utils.Constants
import com.example.tripguide.utils.Constants.TAG
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
    private lateinit var viewModel: SelectViewModel

    var type = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("listType") { key, bundle ->
            val result = bundle.getInt("listTypeKey")
            type = result
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // View Model 설정
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            SelectViewModel::class.java)

        mBinding = FragmentStationDialogBinding.inflate(inflater, container, false)
        return binding.root
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
                stationList.clear()
                keyword = binding.tripName.text.toString().replace(" ", "")
                getResultSearch(keyword)
            }
        })

        // Recycler Item click event
        stationAdapter.setItemClickListener(object : StationAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val result = stationList[position].name.toString()
                setFragmentResult("stationName", bundleOf("stationNameKey" to result))
                viewModel.addTask(SelectItem("", stationList[position].name, type, stationList[position].x.toString(), stationList[position].y.toString()))
                stationList.clear()
                dismiss()
            }
        })
    }

    private fun getResultSearch(keyword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(KakaoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RetrofitKeyword::class.java)
        val call = api.getKakaoKeyword(KakaoApi.API_KEY, keyword)

        call.enqueue(object : Callback<kakaokeyword> {
            override fun onResponse(call: Call<kakaokeyword>, response: Response<kakaokeyword>) {
                Log.d(Constants.TAG, "communication success")
                addItems(response.body())
            }

            override fun onFailure(call: Call<kakaokeyword>, t: Throwable) {
                Log.d(Constants.TAG, "error : " + t.message)
            }
        })
    }


    private fun addItems(searchResult: kakaokeyword?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            // Search results available
            for (document in searchResult!!.documents) {
                Log.d(Constants.TAG, "StationDialogFragment - addItems() called")
                val item = Station(document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble())
                Log.d(TAG, "item - $item")
                stationList.add(item)
                stationAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}