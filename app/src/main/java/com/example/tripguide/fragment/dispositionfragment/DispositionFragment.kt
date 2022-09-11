/*
 This fragment appears when you click the "Create travel itinerary" button in the MainFragment,
 and receives the most basic travel information.
*/
package com.example.tripguide.fragment.dispositionfragment


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.tripguide.R
import com.example.tripguide.utils.Constants.TAG
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.example.tripguide.MainActivity
import com.example.tripguide.TripGuide
import com.example.tripguide.databinding.FragmentDispositionBinding
import com.example.tripguide.fragment.fbAuth
import com.example.tripguide.fragment.fbFirestore
import java.text.SimpleDateFormat
import java.util.*


class DispositionFragment : Fragment(), View.OnClickListener {
    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var mBinding: FragmentDispositionBinding? = null
    private val binding get() = mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentDispositionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar: Calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis // 현재 시간
        calendar.add(Calendar.DATE, +1)
        val tomorrow = calendar.timeInMillis // 내일
        val dataFormat = SimpleDateFormat("M월 d일")
        binding.daterangeview.text = "${dataFormat.format(currentTime)} ~ ${dataFormat.format(tomorrow)}"

        binding.viewdepart.setOnClickListener(this)
        binding.viewarrive.setOnClickListener(this)
        binding.daterangeview.setOnClickListener(this)
        binding.nextbtn.setOnClickListener(this)
        binding.beforebtn.setOnClickListener(this)


    }



    override fun onClick(v: View?) {
        var userInfo = TripGuide()
        userInfo.uid = fbAuth?.uid
        userInfo.userId = fbAuth?.currentUser?.email
        userInfo.timestamp = System.currentTimeMillis()
        userInfo.tripName = binding.tripName.text.toString()
        userInfo.departure = binding.viewdepart.text.toString()
        userInfo.arrival = binding.viewarrive.text.toString()
        userInfo.date = binding.daterangeview.text.toString()
        userInfo.with = binding.tripwith.checkedChipId.toString()
        userInfo.style = binding.tripstyle.checkedChipId.toString()

        fbFirestore?.collection("basic_information")?.document(fbAuth?.uid.toString())?.set(userInfo)

        when(v?.id) {
            R.id.viewdepart -> {
                mainActivity.changeFragment(2)
                Log.d(TAG, "출발지 입력으로 이동")
                setFragmentResultListener("requestKey") { key, bundle ->
                    val result = bundle.getString("bundleKey")
                    binding.viewdepart.text = result
                }
                val hint = "어디에서 여행을 시작하세요?"
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
            }
            R.id.viewarrive -> {
                mainActivity.changeFragment(2)
                Log.d(TAG, "도착지 입력으로 이동")
                setFragmentResultListener("requestKey") { key, bundle ->
                    val result = bundle.getString("bundleKey")
                    binding.viewarrive.text = result
                }
                val hint = "어디로 여행를 가시나요?"
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))

            }
            R.id.daterangeview -> {
                val dateRangePicker =
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("검색 기간을 골라주세요")
                    .setSelection(
                        Pair(
                            MaterialDatePicker.thisMonthInUtcMilliseconds(),
                            MaterialDatePicker.todayInUtcMilliseconds()
                        )
                    )
                        .build()

                dateRangePicker.show(childFragmentManager, "date_picker")
                dateRangePicker.addOnPositiveButtonClickListener { selection ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = selection?.first ?: 0
                    val startDate = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()

                    calendar.timeInMillis = selection?.second ?: 0
                    val endDate = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()

                    binding.daterangeview.text = dateRangePicker.headerText

                }
            }
            R.id.nextbtn -> {
                mainActivity.changeFragment(5)
                var userInfo = TripGuide()
                userInfo.uid = fbAuth?.uid
                userInfo.userId = fbAuth?.currentUser?.email
                userInfo.timestamp = System.currentTimeMillis()
                userInfo.tripName = binding.tripName.text.toString()
                userInfo.departure = binding.viewdepart.text.toString()
                userInfo.arrival = binding.viewarrive.text.toString()
                userInfo.date = binding.daterangeview.text.toString()

                fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.set(userInfo)
            }
            R.id.beforebtn -> {
                mainActivity.changeFragment(6)
            }
        }
    }
    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}