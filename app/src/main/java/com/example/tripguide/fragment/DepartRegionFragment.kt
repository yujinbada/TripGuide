/*
 This fragment is a fragment that receives the correct region name
 when you enter a search term using the Kakao api.
 We show the list through the recycler view when it is searched
 and also set an event when it is clicked.
*/

package com.example.tripguide.fragment

import android.content.Context
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
import com.example.tripguide.retrofit.RetrofitInterface
import com.example.tripguide.utils.Constants.TAG
import com.example.tripguide.utils.KakaoApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

        binding.tripName.text?.clear()
        modelList.clear()

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
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }
                else {
                    Log.d(TAG, "departresult <- region_2depth_txt")
                    val result = modelList[position].secondregion
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }
                modelList[position].x
                modelList[position].y

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
                    document.address.region_2depth_name, document.x, document.y)
                modelList.add(item)
                myRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onDestroyView() {
        mBinding = null
        modelList.clear()
        keyword = ""
        super.onDestroyView()
    }
}