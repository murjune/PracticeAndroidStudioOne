package org.techtown.seminar2.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// 데이터의 변경
// 뷰모델은 데이터의 변경사항을 알려주는 라이브 데이터를 가지고 있고
class MyNumberViewModel(
    _cnt: Int,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    // saveState는 key-value형태로 값을 저장한다.
    // 따라서, 저장과 보관에 사용할 key-value를 설정한다.

    // 저장된게 없으면 초기값으로 세팅
    var cnt = savedStateHandle.get<Int>(SAVE_STATE_KEY) ?: _cnt

    // cnt를 savedStateHandle에 저장하는 함수
    fun saveState() {
        savedStateHandle.set(SAVE_STATE_KEY, cnt)
    }

    companion object {
        private const val SAVE_STATE_KEY = "counter"
    }
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
