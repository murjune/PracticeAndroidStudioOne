package org.techtown.seminar2.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.techtown.seminar2.R

class CoroutinActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutin)

        Log.d(TAG, Thread.currentThread().name)
        val th = Thread(
            Runnable {
                Log.d(TAG, Thread.currentThread().name)
                GlobalScope.launch {
                    Log.d(TAG, Thread.currentThread().name) //
                    Log.d(TAG, "1 ${Thread.currentThread().name}")
                    delay(3000L)
                    Log.d(TAG, "2 ${Thread.currentThread().name}")
                    doNetworkCall()
                    doNetworkCall2()
                }
            }
        )
        th.start()
    }

    // suspend function 활용
    suspend fun doNetworkCall(): String {
        delay(3000L)
        Log.d(TAG, "3 ${Thread.currentThread().name}")
        return "this is the answer"
    }

    suspend fun doNetworkCall2(): String {
        delay(3000L)
        Log.d(TAG, "4 ${Thread.currentThread().name}")
        return "this is the answer"
    }
}
