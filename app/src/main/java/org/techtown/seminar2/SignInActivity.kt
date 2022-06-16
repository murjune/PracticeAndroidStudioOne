package org.techtown.seminar2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.techtown.seminar2.databinding.ActivitySignInBinding
import org.techtown.seminar2.util.Constants.TAG
import org.techtown.seminar2.util.onMyTextChanged
import org.techtown.seminar2.util.showToast

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "MainActivity - onCreate() called")

        // 라디오 그룹 가져오기
        activeRadioButton()
        setOnClickButton()
        addTextChangedListener()
    }

    private fun addTextChangedListener() {
        binding.edtSearchImage.onMyTextChanged {
            if (it!!.isNotEmpty()) {
                binding.layoutTextField.helperText = "" // hint 없애기
                binding.layoutSearchButton.visibility = View.VISIBLE // 검색버튼보여주기
                binding.scvSignIn.scrollTo(0, 200) // 글자가 하나라도 있으면 스크롤뷰 올리기
            }
            if (it.toString().count() == 12) {
                showToast("검색어는 12자까지 가능합니다.")
            }
        }
    }

    private fun setOnClickButton() {
        binding.btnSearch.setOnClickListener {
            handleSearchButtonUi()
        }
    }

    private fun activeRadioButton() {
        binding.rdbPhotoSearch.setOnCheckedChangeListener { _, _ ->
            binding.layoutTextField.hint = "사진검색"
            binding.layoutTextField.setStartIconDrawable(R.drawable.ic_person_search_24)
        }

        binding.rdbUserSearch.setOnCheckedChangeListener { _, _ ->
            binding.layoutTextField.hint = "사용자검색 버튼 클릭"
            binding.layoutTextField.setStartIconDrawable(R.drawable.ic_image_search_24)
        }
    }

    private fun handleSearchButtonUi() {
        binding.progressSearch.visibility = View.VISIBLE
        binding.btnSearch.visibility = View.INVISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressSearch.visibility = View.INVISIBLE
            binding.btnSearch.visibility = View.VISIBLE
        }, 1500)
    }
}
