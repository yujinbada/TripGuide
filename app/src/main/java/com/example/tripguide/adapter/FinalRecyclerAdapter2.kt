package com.example.tripguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripguide.R
import com.example.tripguide.model.FinalItem
import com.example.tripguide.model.SelectItem
import java.time.LocalTime

class FinalRecyclerAdapter2(private var finalRoute : ArrayList<FinalItem>)
    : RecyclerView.Adapter<FinalRecyclerAdapter2.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalRecyclerAdapter2.ViewHolder {
        // 연결할 레이아웃 설정
        return FinalRecyclerAdapter2.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.final_route_item2, parent, false))
    }
    // 목록의 아이템수
    override fun getItemCount(): Int = finalRoute.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.finalTitle)
        val image: ImageView = itemView.findViewById(R.id.finalImg)
        val duration: TextView = itemView.findViewById(R.id.duration)
        val period : TextView = itemView.findViewById(R.id.tripPeriod)
        val appCompatImageView : ImageView = itemView.findViewById(R.id.appCompatImageView)
    }

    override fun onBindViewHolder(holder: FinalRecyclerAdapter2.ViewHolder, position: Int) {
        val item = finalRoute[position]

        holder.title.text = item.selectItem?.title
        holder.period.text

        // duration 설정
        if (item.selectItem?.liveTime == LocalTime.of(8,0,0)) {
            holder.duration.visibility = View.INVISIBLE
            holder.appCompatImageView.visibility = View.INVISIBLE
        }
        else {
        }

        if (item.selectItem?.firstimage != null) {
            Glide.with(holder.image)
                .load(item.selectItem?.firstimage)
                .centerCrop()
                .into(holder.image)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
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