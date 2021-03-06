package com.sion.zhihudailypurified.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    lateinit var ui: B

    lateinit var vm: VM

    protected abstract fun setLayoutId(): Int

    protected abstract fun setViewModel(): Class<out VM>

    protected abstract fun initView()

    protected abstract fun initData()

    open fun onHide() {}

    open fun onShow() {}

    protected open fun recoverData(savedInstanceState: Bundle?) {}

    final override fun onCreate(savedInstanceState: Bundle?) {
        recoverData(savedInstanceState)
        vm = ViewModelProviders.of(this).get(setViewModel())
        super.onCreate(savedInstanceState)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ui = DataBindingUtil.inflate(inflater, setLayoutId(), container, false)
        initView()
        initData()
        return ui.root
    }

    //第一次加载显示并不触发该方法，只在fragmentManager执行hide和show操作时执行
    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            onHide()
        } else {
            onShow()
        }
    }

    open fun back() {
        activity?.onBackPressed()
    }


}