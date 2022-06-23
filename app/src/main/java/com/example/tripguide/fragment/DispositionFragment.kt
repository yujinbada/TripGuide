package com.example.tripguide.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tripguide.R
import com.example.tripguide.utils.Constants.TAG
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_disposition.*
import kotlinx.android.synthetic.main.layout_recycler_item.*
import androidx.core.util.Pair
import com.example.tripguide.MainActivity
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance

class DispositionFragment : Fragment(), View.OnClickListener {
    private val mainActivity = activity as MainActivity?
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_disposition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_depart.setOnClickListener(this)
        view_arriv.setOnClickListener(this)
        date_range_view.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.view_depart -> {
                mainActivity?.changeFragment(2)
                Log.d(TAG, "출발지 입력으로 이동")
                setFragmentResultListener("requestKey") { key, bundle ->
                    val result = bundle.getString("bundleKey")
                    view_depart.text = result
                }
                val hint = "어디에서 여행을 시작하세요?"
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))
            }
            R.id.view_arriv -> {
                mainActivity?.changeFragment(2)
                Log.d(TAG, "도착지 입력으로 이동")
                setFragmentResultListener("requestKey") { key, bundle ->
                    val result = bundle.getString("bundleKey")
                    view_arriv.text = result
                }
                val hint = "어디로 여행를 가시나요?"
                setFragmentResult("hintrequestKey", bundleOf("hintbundleKey" to hint))

            }
            R.id.date_range_view -> {
                val dateRangePicker =
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("검색 기간을 골라주세요")
//                    .setSelection(
//                        Pair(
//                            MaterialDatePicker.thisMonthInUtcMilliseconds(),
//                            MaterialDatePicker.todayInUtcMilliseconds()
//                        )
//                    )
                        .build()

                dateRangePicker.show(childFragmentManager, "date_picker")
                dateRangePicker.addOnPositiveButtonClickListener { selection ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = selection?.first ?: 0
                    var startDate = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()
                    Log.d("start", startDate)

                    calendar.timeInMillis = selection?.second ?: 0
                    var endDate = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()
                    Log.d("end", endDate)

                    date_range_view.text = dateRangePicker.headerText
                }
            }
        }

    }
}