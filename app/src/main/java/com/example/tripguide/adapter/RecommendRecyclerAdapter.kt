package com.example.tripguide.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.example.tripguide.R
import com.example.tripguide.model.RecommendItem
import com.example.tripguide.model.SelectItem
import com.example.tripguide.utils.Constants.TAG
import org.w3c.dom.Text

class RecommendRecyclerAdapter(var items : ArrayList<RecommendItem>) :
    RecyclerView.Adapter<RecommendRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendRecyclerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_recycler_item, parent, false))
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = items.size

    // 전달받은 위치의 아이템 연결
    override fun onBindViewHolder(holder: RecommendRecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        if (item.recommendTitle != null) {
            holder.tvTitle.text = item.recommendTitle
            holder.tvOverView.text = item.tourOverView

            Glide.with(holder.imgFirstImage)
                .load(item.recommendImage)
                .centerCrop()
                .error(R.drawable.ic_launcher_foreground)                  // 오류 시 이미지
                .into(holder.imgFirstImage)

            holder.itemView.setOnClickListener {
                clickListener.onClick(it, position)
            }
        }
    }

    // 뷰 홀더 설정
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFirstImage: ImageView = itemView.findViewById(R.id.recommendImage)    // 이미지
        val tvTitle: TextView = itemView.findViewById(R.id.recommendTitle)           // 제목
        val tvOverView : TextView = itemView.findViewById(R.id.recommendContent)

    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.clickListener = onItemClickListener
    }

    private lateinit var clickListener: OnItemClickListener
}


