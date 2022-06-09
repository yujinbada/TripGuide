package fragment.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tripguide.R
import com.example.tripguide.model.Region


class DepartRegionFragment : Fragment() {

    var regionList = ArrayList<Region>()

    var region = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_depart_region, container, false)
    }
}

