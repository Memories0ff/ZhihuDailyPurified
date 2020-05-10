package com.sion.zhihudailypurified.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
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
        vm.isOnline.value = isOnline()
        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //WiFi是否连接
    fun isWiFiOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
        return networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }

    //移动数据是否连接
    fun isCellularOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
        return networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    }

    //网络是否连接
    fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getNetworkCapabilities(connMgr.activeNetwork) ?: return false
        for (i in listOf(
            NetworkCapabilities.TRANSPORT_WIFI,
            NetworkCapabilities.TRANSPORT_CELLULAR,
            NetworkCapabilities.TRANSPORT_ETHERNET
        )) {
            if (networkInfo.hasTransport(i)) {
                return true
            }
        }
        return false
    }

}