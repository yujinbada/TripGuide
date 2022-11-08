package com.example.tripguide.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripguide.R
import com.example.tripguide.model.FinalItem
import com.example.tripguide.model.SelectItem
import com.example.tripguide.utils.Constants.TAG

class FinalRecyclerAdapter1 (private var finalRoute : ArrayList<FinalItem>)
    : RecyclerView.Adapter<FinalRecyclerAdapter1.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalRecyclerAdapter1.ViewHolder {
        // 연결할 레이아웃 설정
        Log.d(TAG, "FinalRecyclerAdapter1 - onCreateViewHolder() called")
        return FinalRecyclerAdapter1.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.final_route_item, parent, false))
    }
    // 목록의 아이템수
    override fun getItemCount(): Int = finalRoute.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tripDay: TextView = itemView.findViewById(R.id.tripDay)
        val finalRecyclerView = itemView.findViewById<RecyclerView>(R.id.finalRecyclerView)
    }

    override fun onBindViewHolder(holder: FinalRecyclerAdapter1.ViewHolder, position: Int) {
        val item = finalRoute[position]
        val date = item.date?.toCharArray()

        with(holder) {
            tripDay.text = "${date!![0]}${date!![1]} / ${date!![2]}${date!![3]}"
            finalRecyclerView.apply {
                adapter = FinalRecyclerAdapter2(finalRoute)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}