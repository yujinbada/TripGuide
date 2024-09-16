package com.example.tripguide

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tripguide.utils.Constants.TAG

enum class ActionType {
    PLUS, MINUS
}
// 데이터의 변경 뷰 모델은 데이터의 변경사항을 알려주는 라이브 데이터를 가지고 있다.
class MyViewModel {
    // 뮤터블 라이브 데이터 - 수정 가능
    // 라이브 데이터 - 값 변동 안됨

    // 내부에서 설정한느 자료들은 뮤터블로 변경 가능하도록 설정
    private val _currentValue = MutableLiveData<Int>()

    val currentValue: LiveData<Int>
        get() = _currentValue

    init {
        Log.d(TAG, "ViewModel - () called")
        _currentValue.value = 0
    }

    fun updateValue(actionType: ActionType, input: Int){
        when(actionType) {
            ActionType.PLUS -> {
                _currentValue.value = _currentValue.value?.plus(input)
            }
            ActionType.MINUS -> {
                _currentValue.value = _currentValue.value?.minus(input)
            }
        }
    }
}