package com.sion.zhihudailypurified.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.sion.zhihudailypurified.utils.toast

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    lateinit var ui: B
    lateinit var vm: VM

    protected abstract fun setLayoutId(): Int

    protected abstract fun setViewModel(): VM

    protected abstract fun initView()

    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = DataBindingUtil.setContentView(this, setLayoutId())
        vm = setViewModel()
        vm.toastMessage.observe(this, Observer {
            toast(it)
        })
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

}