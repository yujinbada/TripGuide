package com.example.tripguide.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripguide.R

class FirstFragment : Fragment() {
    val TAG: String = "로그"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "FirstFragment - onCreateView() called")

        return inflater.inflate(R.layout.fragment_first, container, false)
    }

}

