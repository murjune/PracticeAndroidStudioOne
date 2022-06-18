package org.techtown.seminar2.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast

// TODO 폴더링 꼭하기

// String? 익스텐션

// JSON 객체가 맞는가?
fun String?.isJsonObject(): Boolean = this?.startsWith("{") == true && this.endsWith("}")

// JSON 리스트가 맞는가?
fun String?.isJsonArray(): Boolean = this?.startsWith("[") == true && this.endsWith("]")

// editText에 대한 익스텐션
fun EditText.onMyTextChanged(completion: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            completion(editable)
        }
    })
}

// Toast
fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
