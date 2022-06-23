package org.techtown.seminar2.presentation.viewmodel

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

// 속성값을 2개이상 넣어주고 싶을 때는 다음과 같이 쓴다.
// 아래 3가지 속성을 동시에 관찰하면서, 변화가 있을 때
// progress바의 증가량 or 감소량을  cnt의 2배로 하고, 임계치값 설정
@BindingAdapter(value = ["app:progressScaled", "android:max", "android:min"], requireAll = true)
fun setProgress(progressBar: ProgressBar, count: Int, max: Int, min: Int) {
    progressBar.progress = (count * 2).coerceIn(min, max)
}
// Int클래스의 Extension
// public fun Int.coerceIn(minimumValue: Int, maximumValue: Int): Int {
//    if (minimumValue > maximumValue) throw IllegalArgumentException("Cannot coerce value to an empty range: maximum $maximumValue is less than minimum $minimumValue.")
//    if (this < minimumValue) return minimumValue
//    if (this > maximumValue) return maximumValue
//    return this
// }
