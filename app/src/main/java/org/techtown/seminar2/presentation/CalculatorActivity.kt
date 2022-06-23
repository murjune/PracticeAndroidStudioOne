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
    // view 커스터마이징하는 custom 바인딩어뎁터 만들기!
    private val myNumberViewModel by viewModels<MyNumberViewModel>() {
        MyNumberViewModelFactory(10, this)
    }
    private lateinit var binding: ActivityCarculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_carculator)
        binding.viewmodel = myNumberViewModel
        binding.lifecycleOwner = this@CalculatorActivity
    }
}
