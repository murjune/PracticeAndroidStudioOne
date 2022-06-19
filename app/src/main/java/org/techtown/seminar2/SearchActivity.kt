package org.techtown.seminar2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonElement
import org.techtown.seminar2.data.api.RetrofitClient
import org.techtown.seminar2.data.entry.search.ResponseState
import org.techtown.seminar2.databinding.ActivitySearchBinding
import org.techtown.seminar2.util.Constants.TAG
import org.techtown.seminar2.util.onMyTextChanged
import org.techtown.seminar2.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "SearchActivity - onCreate() called")

        // 라디오 그룹 가져오기
        activeRadioButton()
        setOnClickButton()
        addTextChangedListener()
    }

    private fun searchPhotos(
        searchTerm: String?,
        completion: (responseState: ResponseState, msg: String) -> Unit
    ) {
        Log.d(TAG, "SearchActivity - searchPhotos() called")
        val term: String = searchTerm ?: ""
        val call: Call<JsonElement> = RetrofitClient.photoService.searchPhotos(term)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "SearchActivity - onResponse() called/ response : ${response.body()}")
                if (response.isSuccessful) {
                    completion(ResponseState.SUCCESS, response.body().toString())
                } else {
                    completion(ResponseState.FAIL, response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "SearchActivity - onFailure() called/ t: $t")
                Log.e("NetworkTest", "error:$t")
            }
        })
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
            searchPhotos(
                binding.edtSearchImage.text.toString(),
                completion = { responseState, responsebody ->
                    when (responseState) {
                        ResponseState.SUCCESS -> {
                            Log.d(TAG, "api 호출 성공: $responsebody")
                        }
                        ResponseState.FAIL -> {
                            showToast("api 호출 error: $responsebody")
                            Log.d(TAG, "api 호출 error: $responsebody")
                        }
                    }
                }
            )
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
