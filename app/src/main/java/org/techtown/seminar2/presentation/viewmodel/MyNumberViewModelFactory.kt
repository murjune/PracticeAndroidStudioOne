package org.techtown.seminar2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MyNumberViewModelFactory(private val cnt: Int): ViewModelProvider.Factory {

    // 뷰모델을 뷰모델팩토리가 받은 다음에 받은 뷰모델의 클래스타입이 MyNumberViewModel이면
    // cnt변수를 뷰모델에 담아서 뷰모델을 반환하겠다라는 로직이다
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyNumberViewModel::class.java)){
            return MyNumberViewModel(cnt) as T
        }
        throw IllegalArgumentException("viewModel class를 찾을 수 없다..")
    }
}