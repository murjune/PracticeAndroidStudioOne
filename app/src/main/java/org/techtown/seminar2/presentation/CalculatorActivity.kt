package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.techtown.seminar2.databinding.ActivityCarculatorBinding
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModel
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModelFactory

// LiveData는 data holder 클래스로 주어진 라이프사이클을 관찰할 수 있다.
// 즉, cnt값이 1 -> 2로 바뀌는 것을 시스템이 감지할 수 있다.
// 뷰모델과 LiveData을 같이 쓸 때 시너지가 난다.
// 기존에는 UI에 표시할 data는 뷰모델에서 가져오고, 값이 변경되면 뷰모델을 확인해서 UI를 변경하는 구조여서
// Activity가 rotate에 의해 재생성되더라도 UI의 값들을 유지할 수 있었다.
// 이제 LiveData라는 데이터홀더로 값이 변경되는 것을 감지해서 UI의 변화를 자동적으로 적용시킬 수 있다.(feat. 데이터바인딩)

// Observer Pattern
// - Subject의 상태 변화를 관찰하는 Observers들을 객체와 연결
// - Subject의 상태 변화를 초래하는 이벤트가 발생시, 객체가 그 이벤트를 OBserver들에게 통지
// 예시? : 유튜브계정주인이 영상을 업로드했을 때, 구독자들에게 푸쉬알림을 보내는 그런 느낌?
// Observer에 의해 값을 감지할 수 있는 안드로이드 data holder로는 'Observable'과 'LiveData'가 있다.
// 1) Observable은 콜백을 등록 (좀 더 공부하자)
//       - lifecyle을 알 수 없음 -> 등록한 콜백이 상시 작동해야함
//       - 작동이 필요 없어지면 `removeOnPropertyChangedCallback`을 호출하여 콜백 수동으로 직접 제거해야함
// 2) LiveData는 lifecycleOwner를 전달하는 과정이 있음
//      - lifecycle이 'Started' or 'Resume'로 활성화 상태에만 observe 수행
//      - 나머지는 자동으로 비활성화
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

        binding.tvResult.text = myNumberViewModel.cnt.toString()
        // update 로직: LiveCount를 옵저빙해서 UI표시하는 로직을 버튼안에 둘 필요가 읎다
        myNumberViewModel.liveCounter.observe(this) { counter ->
            binding.tvResult.text = counter.toString()
        }
        // 동일한 로직
//        myNumberViewModel.liveCounter.observe(
//            this, object : Observer<Int> {
//                override fun onChanged(count: Int?) {
//                    binding.tvResult.text = count.toString()
//                }
//            }
//        )
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
