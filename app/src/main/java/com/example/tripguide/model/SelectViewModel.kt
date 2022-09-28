package com.example.tripguide.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectViewModel : ViewModel() {

    val selectList = MediatorLiveData<List<SelectItem>>()
    private var datas = arrayListOf<SelectItem>()
    val tourList = MutableLiveData<List<SelectItem>>()
    val foodList = MutableLiveData<List<SelectItem>>()
    val hotelList = MutableLiveData<List<SelectItem>>()

    init{
        selectList.addSource(tourList){
                value -> selectList.value = value
        }
        selectList.addSource(foodList){
                value -> selectList.value = value
        }
        selectList.addSource(hotelList){
                value -> selectList.value = value
        }
    }

    fun addTask(selectItem : SelectItem){
        datas.add(selectItem)
        setData(datas)
    }

    fun deleteTask(selectItem : SelectItem){
        datas.remove(selectItem)
        setData(datas)
    }

    private fun setData(data: ArrayList<SelectItem>){
        tourList.value = data.filter { x -> x.type == 12 }.toList()
        foodList.value = data.filter { x -> x.type == 39  }.toList()
        hotelList.value = data.filter { x -> x.type == 32  }.toList()
        selectList.value = data
    }
}