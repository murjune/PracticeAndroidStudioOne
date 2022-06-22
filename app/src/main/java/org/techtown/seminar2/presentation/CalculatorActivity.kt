package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.techtown.seminar2.databinding.ActivityCarculatorBinding

class CalculatorActivity : AppCompatActivity() {
    //    private val myNumberViewModel: MyNumberViewModel by viewModels()
    private lateinit var binding: ActivityCarculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cnt = 100
        binding.tvResult.text = cnt.toString()

        binding.btnPlus.setOnClickListener {
            cnt++
            binding.tvResult.text = cnt.toString()
        }
        binding.btnMinus.setOnClickListener {
            cnt--
            binding.tvResult.text = cnt.toString()
        }
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_carculator)
//        // 뷰 모델을 바인딩 변수로 사용한다.
//        binding.viewmodel = myNumberViewModel
//        // binding에 LifecycleOwner 지정 시 LiveData가 실시간으로 변하는 것을 확인 가능하다
//        binding.lifecycleOwner = this@CalculatorActivity
    }
}
