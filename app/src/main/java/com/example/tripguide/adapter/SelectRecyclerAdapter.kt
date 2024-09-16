package com.example.tripguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.tripguide.R
import com.example.tripguide.databinding.SelectItemBinding
import com.example.tripguide.model.SelectItem

class SelectRecyclerAdapter(var items : List<SelectItem>,
                            val onClickDeleteButton: (selectItem: SelectItem) -> Unit) :
    RecyclerView.Adapter<SelectRecyclerAdapter.ViewHolder>() {

    private lateinit var itemBinding: SelectItemBinding
    // 뷰 홀더 설정
    inner class ViewHolder(private val itemBinding: SelectItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: SelectItem) {
            itemBinding.selecttitle.text = data.title

            if(data.firstimage != null) {
                Glide.with(itemBinding.selectimg)
                    .load(data.firstimage)
                    .centerCrop()
                    .into(itemBinding.selectimg)

            }

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


