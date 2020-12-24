package com.sion.zhihudailypurified.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sion.zhihudailypurified.broadcastReceiver.NetworkReceiver
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

    override fun onResume() {
        registerNetworkReceiver()
        super.onResume()
    }

    override fun onPause() {
        unRegisterNetworkReceiver()
        super.onPause()
    }

    //网络状态监听

    private var networkReceiver: NetworkReceiver? = null

    private fun registerNetworkReceiver() {
        if (networkReceiver == null) {
            networkReceiver = NetworkReceiver().apply {
                networkConnectivityListener = object : NetworkReceiver.NetworkConnectivityListener {
                    override fun onNetworkConnected() {
                        vm.isOnline.value = true
                    }

                    override fun onNetworkDisConnected() {
                        vm.isOnline.value = false
                    }
                }
            }
        }
        val intentFilter = IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }
        registerReceiver(networkReceiver, intentFilter)
    }

    private fun unRegisterNetworkReceiver() {
        unregisterReceiver(networkReceiver)
    }

    /**
     * 是否联网
     */
    fun isOnline(): Boolean {
        return vm.isOnline.value ?: false
    }
}