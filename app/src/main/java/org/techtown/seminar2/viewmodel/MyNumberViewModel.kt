package org.techtown.seminar2.viewmodel

import androidx.lifecycle.ViewModel

// 데이터의 변경
// 뷰모델은 데이터의 변경사항을 알려주는 라이브 데이터를 가지고 있고
class MyNumberViewModel : ViewModel() {
    var cnt = 100
//    // 1. Mutable Live Data - 수정 가능한 녀석
//    var inputText = MutableLiveData<String>("")
//    private val _currentValue = MutableLiveData<Int>(0)
//
//    // 2. Live Data - 값 변동 X(읽기 전용)
//    val currentValue: LiveData<Int> get() = _currentValue
//    fun increase() {
//        val plusCount = inputText.value!!.toInt()
//        _currentValue.value = _currentValue.value?.plus(plusCount)
//        //  Log.d(TAG, "MyNumberViewModel - increase() called ${currentValue.value}")
//    }
//
//    fun decrease() {
//        val minusCount: Int = inputText.value!!.toInt()
//        _currentValue.value = _currentValue.value?.minus(minusCount)
//        // Log.d(TAG, "MyNumberViewModel - decrease() called ${currentValue.value}")
//    }
}
