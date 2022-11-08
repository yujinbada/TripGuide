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
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.tripguide.R
import com.example.tripguide.utils.Constants.TAG
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tripguide.MainActivity
import com.example.tripguide.model.TripGuide
import com.example.tripguide.databinding.FragmentDispositionBinding
import com.example.tripguide.fragment.dialogfragment.DepartRegionFragment
import com.example.tripguide.fragment.dialogfragment.StationDialogFragment
import com.example.tripguide.fragment.fbAuth
import com.example.tripguide.fragment.fbFirestore
import com.example.tripguide.model.SelectViewModel
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
    private lateinit var viewModel: SelectViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
                SelectViewModel::class.java)

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

        binding.viewarrive.setOnClickListener(this)
        binding.viewdepart.setOnClickListener(this)
        binding.daterangeview.setOnClickListener(this)
        binding.nextbtn.setOnClickListener(this)
        binding.beforebtn.setOnClickListener(this)
        binding.alone.setOnClickListener(this)
        binding.couple.setOnClickListener(this)
        binding.over5.setOnClickListener(this)

    }



    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.viewarrive -> {
                mainActivity.dialogFragment(DepartRegionFragment())
                Log.d(TAG, "도착지 입력으로 이동")
                setFragmentResultListener("requestKey") { key, bundle ->
                    val result = bundle.getString("bundleKey")
                    binding.viewarrive.text = result
                }
            }
            R.id.viewdepart -> {
                mainActivity.dialogFragment(StationDialogFragment())
                val type = 1
                setFragmentResult("listType", bundleOf("listTypeKey" to type))

                setFragmentResultListener("stationName") { key, bundle ->
                    val result = bundle.getString("stationNameKey")
                    binding.viewdepart.text = result
                }
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
                    val startDate = SimpleDateFormat("MMdd").format(calendar.time).toString()
                    viewModel.setStartDate(startDate)
                    calendar.timeInMillis = selection?.second ?: 0
                    val endDate = SimpleDateFormat("MMdd").format(calendar.time).toString()
                    viewModel.setEndDate(endDate)
                    Log.d(TAG, "Date - $startDate ~ $endDate")
                    binding.daterangeview.text = dateRangePicker.headerText

                }
            }
            R.id.nextbtn -> {
                if(binding.viewarrive.text == "도착지") {
                    Toast.makeText(activity, "도착지를 선택해 주세요!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if(binding.viewdepart.text == "출발지 주소") {
                        Toast.makeText(activity, "출발지 주소를 선택해 주세요!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val calendar: Calendar = Calendar.getInstance()
                        val currentTime = calendar.timeInMillis // 현재 시간
                        calendar.add(Calendar.DATE, +1)
                        val tomorrow = calendar.timeInMillis // 내일
                        val dataFormat = SimpleDateFormat("M월 d일")

                        if(binding.daterangeview.text == "${dataFormat.format(currentTime)} ~ ${dataFormat.format(tomorrow)}") {
                            Toast.makeText(activity, "여행 기간을 선택해 주세요!", Toast.LENGTH_SHORT).show()
                        }
                        else mainActivity.addFragment(DispositionFragment(), DispositionFragment2())


                    }
                }
//                var userInfo = TripGuide()
//                userInfo.uid = fbAuth?.uid
//                userInfo.userId = fbAuth?.currentUser?.email
//                userInfo.timestamp = System.currentTimeMillis()
//                userInfo.tripName = binding.tripName.text.toString()
//                userInfo.arrival = binding.viewarrive.text.toString()
//                userInfo.date = binding.daterangeview.text.toString()
//                userInfo.with = binding.tripwith.checkedChipId.toString()
//
//                fbFirestore?.collection("basic_information")?.document(fbAuth?.uid.toString())?.set(userInfo)
            }
            R.id.beforebtn -> {
                mainActivity.changeFragment(4)
            }
        }
    }
    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}