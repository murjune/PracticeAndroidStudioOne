package org.techtown.seminar2.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.techtown.seminar2.R

class PracticeActivity : AppCompatActivity() {
    val TAG = "로그"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)
        Log.d(TAG, "5: ${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "1: ${Thread.currentThread().name}")
            Log.d(TAG, "1: ${Thread.currentThread().name}")
            Log.d(TAG, "1: ${Thread.currentThread().name}")
            GlobalScope.launch {
                delay(1000L) // 코루틴스코프 탈출
                Log.d(TAG, "2: ${Thread.currentThread().name}")
            }
            GlobalScope.launch { // 이 코루틴 빌더가 스레드를 블락합니다.
                delay(2000L) // 코루틴스코프 탈출
                Log.d(TAG, "3: ${Thread.currentThread().name}")
            }
            delay(3000L) // 코루틴스코프 탈출
            Log.d(TAG, "4: ${Thread.currentThread().name}")
        }
        Thread.sleep(1000L)
        Log.d(TAG, "5: ${Thread.currentThread().name}")
        Log.d(TAG, "5: ${Thread.currentThread().name}")
        Log.d(TAG, "5: ${Thread.currentThread().name}")
    }
}
