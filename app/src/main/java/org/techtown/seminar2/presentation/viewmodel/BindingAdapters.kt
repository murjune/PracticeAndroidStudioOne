package org.techtown.seminar2.presentation.viewmodel

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["app:progressScaled", "android:max", "android:min"], requireAll = true)
fun setProgress(progressBar: ProgressBar, count: Int, max: Int, min: Int) {
    progressBar.progress = (count * 2).coerceIn(min, max)
}

