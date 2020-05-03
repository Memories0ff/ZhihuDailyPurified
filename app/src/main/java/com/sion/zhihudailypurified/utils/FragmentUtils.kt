package com.sion.zhihudailypurified.utils

import androidx.fragment.app.Fragment


fun Fragment.toast(s: String) {
    this.activity?.toast(s)
}
