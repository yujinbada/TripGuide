/*
    This viewModel contains information about the places chosen by customer
*/
package com.example.tripguide.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalTime

class SelectViewModel : ViewModel() {

    val selectList = MediatorLiveData<List<SelectItem>>()
    private var datas = arrayListOf<SelectItem>()
    val departRegion = MutableLiveData<List<SelectItem>>()       // departRegion information
    val departStationList = MutableLiveData<List<SelectItem>>()  // departStation information
    val departStationTime = MutableLiveData<LocalTime>()         // departStation Time information
    val arriveStationList = MutableLiveData<List<SelectItem>>()  // arriveStation information
    val arriveStationTime = MutableLiveData<LocalTime>()         // arriveStation Time information
    val tourList = MutableLiveData<List<SelectItem>>()           // tourist attractions information
    val foodList = MutableLiveData<List<SelectItem>>()           // restaurants information
    val hotelList = MutableLiveData<List<SelectItem>>()          // hotels information
    val areaCode : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val sigunguCode : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

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
        selectList.addSource(departRegion){
                value -> selectList.value = value
        }
        selectList.addSource(departStationList){
                value -> selectList.value = value
        }
        selectList.addSource(arriveStationList){
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
    fun setDepartTime(time: LocalTime) {
        departStationTime.value = time
    }
    fun setArriveTime(time: LocalTime) {
        arriveStationTime.value = time
    }

    private fun setData(data: ArrayList<SelectItem>){
        tourList.value = data.filter { x -> x.type == 12 }.toList()    // type 12 has information related to travel destinations
        foodList.value = data.filter { x -> x.type == 39  }.toList()   // type 39 has information related to restaurant destinations
        hotelList.value = data.filter { x -> x.type == 32  }.toList()  // type 32 has information related to hotel destinations
        departRegion.value = data.filter { x -> x.type == 1 }.toList() // type 1 has information related to departRegion
        departStationList.value = data.filter { x -> x.type == 2 }.toList()  // type 2 has information related to departStation
        arriveStationList.value = data.filter { x -> x.type == 3 }.toList()  // type 2 has information related to departStation
        selectList.value = data
    }
}