package com.sion.zhihudailypurified.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    //toast
    var toastMessage = MutableLiveData<String>()

    fun toast(s: String) {
        toastMessage.value = s
    }

    //网络状态改变时进行操作
    val isOnline = MutableLiveData<Boolean>()

}