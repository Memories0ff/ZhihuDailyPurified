package com.sion.zhihudailypurified.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    //toast
    var toastMessage = MutableLiveData<String>()

    fun toast(s: String) {
        toastMessage.value = s
    }

    //只在网络状态改变时进行操作
    val isOnline = object : MutableLiveData<Boolean>() {
        override fun setValue(value: Boolean?) {
            if (getValue() != value) {
                super.setValue(value)
            }
        }
    }

}