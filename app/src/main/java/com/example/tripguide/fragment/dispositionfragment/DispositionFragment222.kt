package com.example.tripguide.fragment.dispositionfragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.tripguide.MainActivity
import com.example.tripguide.R
import com.example.tripguide.fragment.dialogfragment.StationDialogFragment
import com.example.tripguide.databinding.FragmentDisposition222Binding
import com.example.tripguide.model.SelectViewModel
import java.time.LocalTime

class DispositionFragment222 : Fragment(), View.OnClickListener {
    // To get the main activity's change fragment function
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    private var mBinding: FragmentDisposition222Binding? = null
    private val binding get() = mBinding!!
    private lateinit var viewModel: SelectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            SelectViewModel::class.java)

        mBinding = FragmentDisposition222Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.departName.setOnClickListener(this)
        binding.arriveName.setOnClickListener(this)
        binding.nextbtn222.setOnClickListener(this)
        binding.beforebtn222.setOnClickListener(this)

        binding.departtimepicker.setOnTimeChangedListener{view, hour, min ->
            viewModel.setDepartTime(LocalTime.of(hour, min, 0))
            }
        binding.arrivetimepicker.setOnTimeChangedListener{view, hour, min ->
            viewModel.setArriveTime(LocalTime.of(hour, min, 0))
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.departName -> {
                mainActivity.dialogFragment(StationDialogFragment())
                setFragmentResultListener("stationName") { key, bundle ->
                    val result = bundle.getString("stationNameKey")
                    binding.departName.text = result
                }
                val type = 2
                setFragmentResult("listType", bundleOf("listTypeKey" to type))
            }
            R.id.arriveName -> {
                mainActivity.dialogFragment(StationDialogFragment())
                setFragmentResultListener("stationName") { key, bundle ->
                    val result = bundle.getString("stationNameKey")
                    binding.arriveName.text = result
                }
                val type = 3
                setFragmentResult("listType", bundleOf("listTypeKey" to type))
            }
            R.id.nextbtn222 -> {
                mainActivity.addFragment(DispositionFragment222(), DispositionFragment22())
            }
            R.id.beforebtn222 -> {
                mainActivity.removeFragment(DispositionFragment2())
            }
        }
    }
}