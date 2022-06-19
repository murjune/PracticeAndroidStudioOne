package org.techtown.seminar2.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.techtown.seminar2.data.api.LottoClient
import org.techtown.seminar2.data.entry.search.lotto.ResponseLottoNum
import org.techtown.seminar2.databinding.ActivityLottoBinding
import org.techtown.seminar2.util.Constants.TAG
import org.techtown.seminar2.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LottoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLottoBinding
    private var lottoNums = mutableListOf<Int>()
    private lateinit var myLottoNum: MutableList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLottoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNetWork()
        onClickButton()
    }

    private fun onClickButton() {
        binding.btnResult.setOnClickListener {
            if (lottoNums.isNotEmpty()) {
                createMyLottoNum()
                val result = getLottoResult()
                changeMyLottoNum()
                binding.tvWinning.text = "$lottoNums \n $result"
            } else {
                showToast("아직 서버에서 로또번호 못가져옴 ㅜ")
            }
        }
    }
    private fun changeMyLottoNum() {
        with(binding) {
            tvNum1.text = myLottoNum[0].toString()
            tvNum2.text = myLottoNum[1].toString()
            tvNum3.text = myLottoNum[2].toString()
            tvNum4.text = myLottoNum[3].toString()
            tvNum5.text = myLottoNum[4].toString()
            tvNum6.text = myLottoNum[5].toString()
        }
    }
    private fun createMyLottoNum() {
        Log.d(TAG, "LottoActivity - createLottoNum() called")
        val nums: MutableList<Int> = IntArray(45) { it + 1 }.toMutableList()
        nums.shuffle()

        myLottoNum = nums.slice(0..5).toMutableList()
    }

    private fun getLottoResult(): String {
        Log.d(TAG, "LottoActivity - getLottoResult() called")
        Log.d(TAG, "LottoActivity - getLottoResult() - 내 로또번호: $myLottoNum")
        Log.d(TAG, "LottoActivity - getLottoResult() - 로또번호 : $lottoNums")
        var cnt = 0
        for (i in 0..5) {
            if (myLottoNum[i].equals(lottoNums[i])) {
                cnt++
            }
        }
        return when (cnt) {
            6 -> "1등"
            5 -> "2등"
            4 -> "3등"
            3 -> "4등"
            2 -> "5등"
            1 -> "6등"
            else -> "아무것도 없쥬?"
        }
    }

    private fun initNetWork() {
        Log.d(TAG, "LottoActivity - initNetWork() called")
        val call: Call<ResponseLottoNum> =
            LottoClient.lottoService.responseLottoInfo(ROUND)
        call.enqueue(object : Callback<ResponseLottoNum> {
            override fun onResponse(
                call: Call<ResponseLottoNum>,
                response: Response<ResponseLottoNum>
            ) {
                Log.d(TAG, "LottoActivity - onResponse() - ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.returnValue == SUCCESS) {
                            lottoNums.add(it.lottoNum_one)
                            lottoNums.add(it.lottoNum_two)
                            lottoNums.add(it.lottoNum_three)
                            lottoNums.add(it.lottoNum_four)
                            lottoNums.add(it.lottoNum_five)
                            lottoNums.add(it.lottoNum_six)
                            Log.d(TAG, "LottoActivity - onResponse() called : network 연결성공")
                        } else {
                            showToast("${it.returnValue} Error입니다.")
                            Log.d(
                                TAG,
                                "LottoActivity - onResponse() called :${it.returnValue} Error입니다."
                            )
                        }
                    }
                } else {
                    showToast("연결 오류 뜸")
                    Log.d(TAG, "${response.code()} error입니다.")
                }
            }

            override fun onFailure(call: Call<ResponseLottoNum>, t: Throwable) {
                Log.d(TAG, "response Code error - NetWork 연결 실패")
                showToast("NetWork 연결 실패")
            }
        })
    }

    companion object {
        const val ROUND = "10"
        const val SUCCESS = "success"
    }
}
