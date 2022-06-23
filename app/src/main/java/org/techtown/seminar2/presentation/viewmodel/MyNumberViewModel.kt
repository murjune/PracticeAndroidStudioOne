package org.techtown.seminar2.presentation.viewmodel

import androidx.lifecycle.*

class MyNumberViewModel(
    _cnt: Int,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _liveCounter: MutableLiveData<Int> = MutableLiveData(_cnt)
    val liveCounter: LiveData<Int> get() = _liveCounter
    val modifiedCounter: LiveData<String> = Transformations.map(liveCounter) { counter ->
        "$counter 입니다."
    }
//    var count = savedStateHandle.get<Int>(SAVE_STATE_KEY) ?: modifiedCounter.value
//
//    private fun saveState() {
//        savedStateHandle.set(SAVE_STATE_KEY, liveCounter.value)
//    }
    fun increaseCount() {
        _liveCounter.value = liveCounter.value?.plus(1)
    }

    fun decreaseCount() {
        _liveCounter.value = liveCounter.value?.minus(1)
    }

//    companion object {
//        private const val SAVE_STATE_KEY = "counter"
//    }
}
