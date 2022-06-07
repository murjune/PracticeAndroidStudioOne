package org.techtown.seminar2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.techtown.seminar2.databinding.ActivitySignInBinding
import org.techtown.seminar2.util.Constants.TAG
import org.techtown.seminar2.util.SearchType

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private var currentSearchType: SearchType = SearchType.PHOTO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "MainActivity - onCreate() called")

        // 라디오 그룹 가져오기
        binding.rdgSearch.setOnCheckedChangeListener { _, _ ->
            Log.d(TAG, "사진검색 버튼 클릭")
            binding.layoutTextField.hint = "사진검색"
            binding.layoutTextField.setStartIconDrawable(R.drawable.ic_person_search_24)
        }
    }
}
