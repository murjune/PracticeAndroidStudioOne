package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.techtown.seminar2.databinding.ActivityCarculatorBinding
import org.techtown.seminar2.viewmodel.MyNumberViewModel

class CalculatorActivity : AppCompatActivity() {
    //    private val myNumberViewModel: MyNumberViewModel by viewModels()
    private lateinit var binding: ActivityCarculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO // 뷰모델에 따로 cnt라는 변수를 가져오도록 했지만, Activity에서 cnt = 100으로 지정하는 로직이 있기 때문에
        //        // Activity가 재생산될 때마다 다시 100으로 돌아간다.
        //        // 이 문제를 해결하고자 다음 단계에서는 ViewModel에 초깃값을 건네주고, 나머지 로직에서는 저장된 값을 사용하도록 하겠다.
        
        val myNumberViewModel = ViewModelProvider(this).get(MyNumberViewModel::class.java)
        myNumberViewModel.cnt = 100
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
