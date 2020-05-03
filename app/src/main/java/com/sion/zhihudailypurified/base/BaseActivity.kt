package com.sion.zhihudailypurified.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.sion.zhihudailypurified.utils.toast

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    lateinit var ui: B
    lateinit var vm: VM

    protected abstract fun setLayoutId(): Int

    protected abstract fun setViewModel(): Class<out VM>

    protected abstract fun initView()

    protected abstract fun initData()

    protected open fun recoverData(savedInstanceState: Bundle?) {

    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recoverData(savedInstanceState)
        ui = DataBindingUtil.setContentView(this, setLayoutId())
        vm = ViewModelProviders.of(this).get(setViewModel())
        vm.toastMessage.observe(this, Observer {
            toast(it)
        })
        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}