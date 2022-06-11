package com.example.tripguide.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripguide.R
import com.example.tripguide.model.MyModel
import com.example.tripguide.utils.Constants.TAG

class MyRecyclerAdapter(var modelList : ArrayList<MyModel>): RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {

    // 뷰홀더가 생성 되었을때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerAdapter.ViewHolder {
        // 연결할 레이아웃 설정
        return MyRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_item, parent, false))
    }

    // 목록의 아이템수
    override fun getItemCount(): Int {
        return this.modelList.size
    }

    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: MyRecyclerAdapter.ViewHolder, position: Int) {
        Log.d(TAG, "MyRecyclerAdapter - onBindViewHolder() called / position: $position")
        holder.region.text = modelList[position].region
        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val region: TextView = itemView.findViewById(R.id.region_txt)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

}