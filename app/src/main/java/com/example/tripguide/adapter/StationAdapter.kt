package com.example.tripguide.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripguide.R
import com.example.tripguide.model.Station
import com.example.tripguide.utils.Constants

class StationAdapter(private var staionList : ArrayList<Station>)
    : RecyclerView.Adapter<StationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationAdapter.ViewHolder {
    // 연결할 레이아웃 설정
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.station_item, parent, false))
}

    // 목록의 아이템수
    override fun getItemCount(): Int = staionList.size

    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = staionList[position].name
        holder.road.text = staionList[position].road
        holder.address.text = staionList[position].address

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_list_name)
        val road: TextView = itemView.findViewById(R.id.tv_list_road)
        val address: TextView = itemView.findViewById(R.id.tv_list_address)
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