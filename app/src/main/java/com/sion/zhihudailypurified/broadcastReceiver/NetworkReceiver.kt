package com.sion.zhihudailypurified.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(this.javaClass.name, "网络状态发生变化")
        networkConnectivityListener?.let {
            if (isOnline(context)) {
                it.onNetworkConnected()
            } else {
                it.onNetworkDisConnected()
            }
        }
    }

    //WiFi是否连接
    private fun isWiFiOnline(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
        return networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }

    //移动数据是否连接
    private fun isCellularOnline(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
        return networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    }

    //网络是否连接
    private fun isOnline(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    companion object
    interface NetworkConnectivityListener {
        fun onNetworkConnected()
        fun onNetworkDisConnected()
    }

    var networkConnectivityListener: NetworkConnectivityListener? = null
}