package org.techtown.seminar2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.techtown.seminar2.databinding.ActivityLottoBinding

class LottoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLottoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO BaseActivity 추가하기
        binding = ActivityLottoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}