package com.sion.zhihudailypurified.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    var toastMessage = MutableLiveData<String>()

    fun toast(s: String) {
        toastMessage.value = s
    }
}