package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.techtown.seminar2.databinding.ActivityCarculatorBinding
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModel
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModelFactory

class CalculatorActivity : AppCompatActivity() {
    private val myNumberViewModel by viewModels<MyNumberViewModel>() {
        MyNumberViewModelFactory(100, this)
    }
    private lateinit var binding: ActivityCarculatorBinding

    // LiveData 적용
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myNumberViewModel.modifiedCounter.observe(this) { counter ->
            binding.tvResult.text = counter.toString()
        }
        binding.btnPlus.setOnClickListener {
            myNumberViewModel.liveCounter.value = myNumberViewModel.liveCounter.value?.plus(1)
            myNumberViewModel.saveState()
        }
        binding.btnMinus.setOnClickListener {
            myNumberViewModel.liveCounter.value = myNumberViewModel.liveCounter.value?.minus(1)
            myNumberViewModel.saveState()
        }
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_carculator)
//        // 뷰 모델을 바인딩 변수로 사용한다.
//        binding.viewmodel = myNumberViewModel
//        // binding에 LifecycleOwner 지정 시 LiveData가 실시간으로 변하는 것을 확인 가능하다
//        binding.lifecycleOwner = this@CalculatorActivity
    }
}
