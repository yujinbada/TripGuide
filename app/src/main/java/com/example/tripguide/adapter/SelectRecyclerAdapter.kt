package com.example.tripguide.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripguide.R
import com.example.tripguide.databinding.SelectItemBinding
import com.example.tripguide.model.RecommendItem
import com.example.tripguide.model.SelectItem
import com.example.tripguide.model.SelectViewModel
import com.example.tripguide.utils.Constants.TAG

class SelectRecyclerAdapter(var items : List<SelectItem>,
                            val onClickDeleteButton: (selectItem: SelectItem) -> Unit) :
    RecyclerView.Adapter<SelectRecyclerAdapter.ViewHolder>() {

    private lateinit var itemBinding: SelectItemBinding
    // 뷰 홀더 설정
    inner class ViewHolder(private val itemBinding: SelectItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: SelectItem) {
            itemBinding.selecttitle.text = data.title

            Glide.with(itemBinding.selectimg)
                .load(data.firstimage)
                .error(R.drawable.ic_launcher_foreground)                  // 오류 시 이미지
                .into(itemBinding.selectimg)

            itemBinding.cancelbutton.setOnClickListener {
                onClickDeleteButton.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemBinding = SelectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = items.size

    // 전달받은 위치의 아이템 연결
    override fun onBindViewHolder(holder: SelectRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun setTourData(tourData: ArrayList<SelectItem>){
        items = tourData
        notifyDataSetChanged()
    }

    fun setFoodData(foodData: ArrayList<SelectItem>){
        items = foodData
        notifyDataSetChanged()
    }

    fun setHotelData(hotelData: ArrayList<SelectItem>){
        items = hotelData
        notifyDataSetChanged()
    }
}


