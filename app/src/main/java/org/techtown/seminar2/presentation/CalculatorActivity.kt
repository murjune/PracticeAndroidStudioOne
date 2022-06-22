package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.techtown.seminar2.databinding.ActivityCarculatorBinding
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModel
import org.techtown.seminar2.presentation.viewmodel.MyNumberViewModelFactory

class CalculatorActivity : AppCompatActivity() {
    //    private val myNumberViewModel: MyNumberViewModel by viewModels()
    private lateinit var binding: ActivityCarculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModelProvider로 뷰모델 객체를 생성시 초기값을 전달하는 것이 금지되어 있기 때문에
        // ViewModelFactory를 사용하도록하자:D
        // 1. 뷰모델 팩토리에 뷰모델에 전달한 인자를 넣어준다.
        val factory = MyNumberViewModelFactory(100)
        // 2. 뷰모델provider의 생성자에 팩토리를 추가인자로 넣어준다
        // 그러면 뷰모델provider가 factory를 통해서 MyNumberViewModel객체를 만들어준다.
        // 이제 화면을 rotate해서 Activity가 파괴되도 뷰모델에서 data값을 가져오기 때문에 값이 변경되지 않는것을 볼 수 있다 :D
        val myNumberViewModel = ViewModelProvider(this, factory).get(MyNumberViewModel::class.java)
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
