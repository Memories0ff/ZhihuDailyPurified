package com.sion.zhihudailypurified.utils

import android.app.Activity
import android.util.Log
import android.widget.Toast

fun Activity.toast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun Activity.logi(s: String) {
    Log.i(this.localClassName, s)
}

fun Activity.logd(s: String) {
    Log.d(this.localClassName, s)
}

fun Activity.loge(s: String) {
    Log.e(this.localClassName, s)
}