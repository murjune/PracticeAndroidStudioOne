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

    // 양방향 데이터바인딩 (지금까지는 viewmodel -> xml(뷰)로만 data를 보냈지만, 이제 xml(뷰) -> viewmodel에 data보낼 수 있다.)
    // checkBox는 UI에서도 checked속성을 바꿔줄 수 있다.
    // 따라서, viewmodel에 hasChecked변수를 만들어준 후
    // two-way 데이터바인딩이 잘 일어나는지 textView로 나타내준다.
    val _hasChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasChecked: LiveData<Boolean> get() = _hasChecked

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
