package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.techtown.seminar2.databinding.ActivityCarculatorBinding
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModel
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModelFactory

class CalculatorActivity : AppCompatActivity() {
    // [공식문서](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=ko)
    // Create a ViewModel the first time the system calls an activity's onCreate() method.
    // Re-created activities receive the same MyViewModel instance created by the first activity.
    // 번역:  ViewModel 객체는 구성이 변경되는 동안 자동으로 보관되므로,
    // 이러한 객체가 보유한 데이터는 다음 활동 또는 프래그먼트 인스턴스에서 즉시 사용할 수 있습니다.
    // 예를 들어 앱에서 사용자 목록을 표시해야 한다면 다음 샘플 코드에 설명된 대로 사용자 목록을 확보하여 활동이나 프래그먼트 대신 ViewModel에 보관하도록 책임을 할당

    // 1. by 키워드로 간단하게 뷰모델 만들기!!(위임작업)
    // Use the 'by viewModels()' Kotlin property delegate
    // from the activity-ktx artifact
    private val myNumberViewModel by viewModels<MyNumberViewModel>() {
        // 2. 팩토리 적용
        MyNumberViewModelFactory(100)
    }

    // 다음과 같이 초기화도 가능함
    // val myNumberViewModel: MyNumberViewModel by viewModels()
    // 2. 팩토리 적용
    private lateinit var binding: ActivityCarculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvResult.text = myNumberViewModel.cnt.toString()

        binding.btnPlus.setOnClickListener {
            myNumberViewModel.cnt++
            binding.tvResult.text = myNumberViewModel.cnt.toString()
        }
        binding.btnMinus.setOnClickListener {
            myNumberViewModel.cnt--
            binding.tvResult.text = myNumberViewModel.cnt.toString()
        }
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_carculator)
//        // 뷰 모델을 바인딩 변수로 사용한다.
//        binding.viewmodel = myNumberViewModel
//        // binding에 LifecycleOwner 지정 시 LiveData가 실시간으로 변하는 것을 확인 가능하다
//        binding.lifecycleOwner = this@CalculatorActivity
    }
}
