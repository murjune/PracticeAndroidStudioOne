package org.techtown.seminar2.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
// class MyNumberViewModelFactory(private val cnt: Int): ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(MyNumberViewModel::class.java)){
//            return MyNumberViewModel(cnt) as T
//        }
//        throw IllegalArgumentException("viewModel class를 찾을 수 없다..")
//    }
// }

// MyNumberViewModelFactory SavedStateHandle을 받을 수 있도록 변

// 변경점: AbstractSavedStateViewModelFactory상속
class MyNumberViewModelFactory(
    private val cnt: Int,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(MyNumberViewModel::class.java)) {
            // 뷰모델 반환할 때, handle도 같이 반환
            return MyNumberViewModel(cnt, handle) as T
        }
        throw IllegalArgumentException("viewModel class를 찾을 수 없다..")
    }
}
