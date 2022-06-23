package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.techtown.seminar2.R
import org.techtown.seminar2.databinding.ActivityCarculatorBinding
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModel
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModelFactory

class CalculatorActivity : AppCompatActivity() {
    private val myNumberViewModel by viewModels<MyNumberViewModel>() {
        MyNumberViewModelFactory(100, this)
    }
    private lateinit var binding: ActivityCarculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1) data바인딩
        binding = DataBindingUtil.setContentView(this, R.layout.activity_carculator)
        // 2) data를 가지고 있는 뷰모델을 지정해준다.
        binding.viewmodel = myNumberViewModel
        // 3) LiveData를 관측하기 위해 lifecycleOwner 설정
        // binding에 LifecycleOwner 지정 시 LiveData가 실시간으로 변하는 것을 확인 가능하다
        binding.lifecycleOwner = this@CalculatorActivity

        binding.btnPlus.setOnClickListener {
            myNumberViewModel.liveCounter.value = myNumberViewModel.liveCounter.value?.plus(1)
            myNumberViewModel.saveState()
        }
        binding.btnMinus.setOnClickListener {
            myNumberViewModel.liveCounter.value = myNumberViewModel.liveCounter.value?.minus(1)
            myNumberViewModel.saveState()
        }
    }
}
