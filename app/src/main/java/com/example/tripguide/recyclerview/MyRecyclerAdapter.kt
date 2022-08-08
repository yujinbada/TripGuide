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
        Log.d(TAG, "MyRecyclerAdapter - onCreateViewHolder() called")
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_item, parent, false))
    }

    // 목록의 아이템수
    override fun getItemCount(): Int = modelList.size

    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: MyRecyclerAdapter.ViewHolder, position: Int) {
        Log.d(TAG, "MyRecyclerAdapter - onBindViewHolder() called / position: $position")
        holder.firstregion.text = modelList[position].firstregion
        holder.secondregion.text = modelList[position].secondregion
        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val firstregion: TextView = itemView.findViewById(R.id.region_1depth_txt)
        val secondregion: TextView = itemView.findViewById(R.id.region_2depth_txt)
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