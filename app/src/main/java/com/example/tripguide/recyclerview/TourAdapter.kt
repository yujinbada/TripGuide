package com.example.tripguide.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripguide.R
import com.example.tripguide.model.Tour

class TourAdapter(var items : ArrayList<Tour>) : RecyclerView.Adapter<TourAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener

    // 뷰 홀더 만들어서 반환, 뷰릐 레이아웃은 list_item_tour.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_tour, parent, false))
    }
    // 아이템 갯수 리턴
    override fun getItemCount() = items.size

    // 전달받은 위치의 아이템 연결
    override fun onBindViewHolder(holder: TourAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvAddr1.text = item.addr1

        Glide.with(holder.imgFirstImage)
                .load(item.firstimage)
                .error(R.drawable.ic_launcher_foreground)                  // 오류 시 이미지
                .into(holder.imgFirstImage)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    // 뷰 홀더 설정
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            val imgFirstImage = itemView.findViewById<ImageView>(R.id.imgfirst)    // 이미지
            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)                 // 제목
            val tvAddr1 = itemView.findViewById<TextView>(R.id.tvAddr1)                 // 주소
    }

    // (2) 리스너 인터페이스
    internal interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    internal fun setItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

}