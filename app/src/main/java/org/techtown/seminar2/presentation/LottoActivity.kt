package org.techtown.seminar2.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.techtown.seminar2.data.api.LottoClient
import org.techtown.seminar2.databinding.ActivityLottoBinding
import org.techtown.seminar2.util.Constants.TAG
import org.techtown.seminar2.util.showToast
import java.util.*

class LottoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLottoBinding
    private val job = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLottoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickButton()
    }

    private fun onClickButton() {
        binding.btnResult.setOnClickListener {
            val myLottoNum = createMyLottoNum()
            changeMyLottoNum(myLottoNum)
            CoroutineScope(Dispatchers.IO + job).launch {
                val winningLottoNums = async { getWinningLottoNums() }
                val rank = getLottoResult(myLottoNum, winningLottoNums.await())
                withContext(Dispatchers.Main) {
                    binding.tvWinning.text = "${winningLottoNums.await()} : $rank"
                }
            }
        }
    }

    private fun changeMyLottoNum(myLottoNum: MutableList<Int>) {
        with(binding) {
            tvNum1.text = myLottoNum[0].toString()
            tvNum2.text = myLottoNum[1].toString()
            tvNum3.text = myLottoNum[2].toString()
            tvNum4.text = myLottoNum[3].toString()
            tvNum5.text = myLottoNum[4].toString()
            tvNum6.text = myLottoNum[5].toString()
        }
    }

    private fun createMyLottoNum(): MutableList<Int> {
        Log.d(TAG, "LottoActivity - createLottoNum() called")
        val nums: MutableList<Int> = IntArray(45) { it + 1 }.toMutableList()
        nums.shuffle()

        return nums.slice(0..5).toMutableList()
    }

    private suspend fun getLottoResult(
        lottoNums: MutableList<Int>?,
        myLottoNums: MutableList<Int>
    ): String {
        if (lottoNums == null) {
            return "네트워크 오류발생"
        }
        var cnt = 0
        for (i in 0..5) {
            if (myLottoNums[i].equals(lottoNums[i])) {
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

    private suspend fun getWinningLottoNums(): MutableList<Int> {
        var WinlottoNums = mutableListOf<Int>()
        val response = LottoClient.lottoService.responseLottoInfo(ROUND)
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.returnValue == SUCCESS) {
                    WinlottoNums.add(it.lottoNum_one)
                    WinlottoNums.add(it.lottoNum_two)
                    WinlottoNums.add(it.lottoNum_three)
                    WinlottoNums.add(it.lottoNum_four)
                    WinlottoNums.add(it.lottoNum_five)
                    WinlottoNums.add(it.lottoNum_six)
                }
            }
            return WinlottoNums
        }
        return mutableListOf()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    companion object {
        const val ROUND = "10"
        const val SUCCESS = "success"
        const val TAG = "로또"
    }
}
