package com.example.tripguide.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripguide.R
import com.example.tripguide.model.FinalItem
import kotlinx.coroutines.NonDisposableHandle.parent
import java.time.format.DateTimeFormatter
import java.util.*

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
        val remove : TextView = itemView.findViewById(R.id.tvRemove)
    }


    override fun onBindViewHolder(holder: FinalRecyclerAdapter2.ViewHolder, position: Int) {
        val item = finalRoute[position]


        val timeFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")


        // duration 설정
        if (position == 0) {
            holder.duration.text = "  오늘의 일정 시작!"
            holder.appCompatImageView.visibility = View.INVISIBLE
            holder.period.text = "${item.selectItem?.liveTime?.format(timeFormatter)}"
        }
        else {
            if (finalRoute[position].selectItem?.type == 2 || finalRoute[position].selectItem?.type == 3){
                val formerLiveTime = finalRoute[position-1].selectItem?.liveTime
                val duration = item.selectItem?.liveTime?.minusHours(formerLiveTime?.hour!!.toLong())
                    ?.minusMinutes(formerLiveTime.minute.toLong())
                holder.duration.text = "이동시간 - ${duration?.hour}시간 ${duration?.minute}분"
                holder.period.text = "${item.selectItem?.liveTime?.format(timeFormatter)}"
            }
            else {
                if(finalRoute[position - 1].selectItem?.type == 1 || finalRoute[position - 1].selectItem?.type == 3) {
                    val formerLiveTime = finalRoute[position-1].selectItem?.liveTime
                    val duration = item.selectItem?.liveTime?.minusHours(formerLiveTime?.hour!!.toLong())
                        ?.minusMinutes(formerLiveTime.minute.toLong())
                    holder.duration.text = "이동시간 - ${duration?.hour}시간 ${duration?.minute}분"
                    holder.period.text = "${item.selectItem?.liveTime?.format(timeFormatter)}\n ~ \n${item.selectItem?.liveTime?.plusMinutes(90)?.format(timeFormatter)}"
                }
                else {
                    val formerLiveTime = finalRoute[position-1].selectItem?.liveTime
                    val duration = item.selectItem?.liveTime?.minusHours(formerLiveTime?.hour!!.toLong())
                        ?.minusMinutes(formerLiveTime.minute.toLong() + 90)
                    holder.duration.text = "이동시간 - ${duration?.hour}시간 ${duration?.minute}분"
                    holder.period.text = "${item.selectItem?.liveTime?.format(timeFormatter)}\n ~ \n${item.selectItem?.liveTime?.plusMinutes(90)?.format(timeFormatter)}"
                }
            }
        }

        if (item.selectItem?.title != "장소 추가") {
            holder.title.text = item.selectItem?.title
            Glide.with(holder.image)
                .load(item.selectItem?.firstimage)
                .centerCrop()
                .into(holder.image)
        }
        else {
            holder.title.text = "이 시간에 다른 장소를 \n 추가 해주세요"
            Glide.with(holder.image)
                .load(R.color.white)
                .centerCrop()
                .into(holder.image)
        }

        holder.image.setOnClickListener {
            itemClickListener.onItemClick(it, position)
        }
        holder.remove.setOnClickListener {
            removeData(position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onItemClick(v: View, childPosition: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener 로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        finalRoute.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position)
    }

    // 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(finalRoute, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }
}