package org.techtown.seminar2.presentation.viewmodel

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

// BindingAdapter: 뷰를 커스터마이징하기 위해서 사용
// 속성값을 어노테이션에 넣는다.
@BindingAdapter("app:progressScaled")
fun setProgress(progressBar: ProgressBar, count: Int) {
    progressBar.progress = count
}
