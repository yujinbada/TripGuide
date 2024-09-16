package com.example.tripguide.adapter

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripguide.R
import com.example.tripguide.fragment.dispositionfragment.DispositionFragment6
import com.example.tripguide.model.FinalItem
import com.example.tripguide.model.GrandFinalItem
import com.example.tripguide.utils.Constants.TAG

class FinalRecyclerAdapter1 (private var grandFinalRoute : ArrayList<GrandFinalItem>)
    : RecyclerView.Adapter<FinalRecyclerAdapter1.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalRecyclerAdapter1.ViewHolder {
        // 연결할 레이아웃 설정
        Log.d(TAG, "FinalRecyclerAdapter1 - onCreateViewHolder() called")
        return FinalRecyclerAdapter1.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.final_route_item, parent, false))
    }
    // 목록의 아이템수
    override fun getItemCount(): Int = grandFinalRoute.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tripDay: TextView = itemView.findViewById(R.id.tripDay)
        val finalRecyclerView = itemView.findViewById<RecyclerView>(R.id.finalRecyclerView)!!
    }


    override fun onBindViewHolder(holder: FinalRecyclerAdapter1.ViewHolder, position: Int) {
        val item = grandFinalRoute[position]

        val date = item.date?.toCharArray()
        val finalRecyclerAdapter2 = FinalRecyclerAdapter2(item.finalItemList)

        with(holder) {
            tripDay.text = "${date!![0]}${date!![1]}월 ${date!![2]}${date!![3]}일"
            finalRecyclerView.apply {
                adapter = finalRecyclerAdapter2
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }

        val swipeHelperCallback = SwipeHelperCallback(finalRecyclerAdapter2).apply {
            setClamp(270F)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(holder.finalRecyclerView)

        holder.finalRecyclerView.setOnClickListener {
            swipeHelperCallback.removePreviousClamp(holder.finalRecyclerView)
            false
        }

        finalRecyclerAdapter2.setItemClickListener(object : FinalRecyclerAdapter2.OnItemClickListener {
            override fun onItemClick(v: View, childPosition: Int) {
                val childItem = item.finalItemList
                rvitemClickListener.onChildItemClick(position, childPosition, childItem)
            }
        })
    }

    private lateinit var rvitemClickListener : DispositionFragment6.RVitemClickListner

    fun setItemClickListener(onItemClickListener: DispositionFragment6.RVitemClickListner) {
        this.rvitemClickListener = onItemClickListener
    }


}