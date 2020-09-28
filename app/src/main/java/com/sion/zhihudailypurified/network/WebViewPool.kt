package com.sion.zhihudailypurified.network

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.withTimeout

class WebViewPool : LifecycleObserver {
    private val available by lazy {
        ArrayList<WebView>()
    }
    private val inUse by lazy {
        ArrayList<WebView>()
    }
    private var context: Context? = null
    private val maxSize = 9
    private var currentSize = 0

    companion object {
        private var instance: WebViewPool? = null

        fun getInstance(): WebViewPool {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = WebViewPool()
                    }
                }
            }
            return instance!!
        }

    }

    @Synchronized
    fun getWebView(): WebView =
        if (available.size > 0) {
            val webViewTemp = available[0]
            available.removeAt(0)
            inUse.add(webViewTemp)
            currentSize++
            logd(
                "执行getWebView, " +
                        "available.size = ${available.size}, " +
                        "inUse.size = ${inUse.size}, " +
                        "currentSize = $currentSize"
            )
            webViewTemp
        } else {
            if (context == null) {
                throw Exception("WebViewPool需要被注册为生命周期观察者")
            }
            val webViewTemp = WebView(context)
            initWebView(webViewTemp)
            inUse.add(webViewTemp)
            currentSize++
            logd(
                "执行getWebView, " +
                        "available已满, " +
                        "available.size = ${available.size}, " +
                        "inUse.size = ${inUse.size}, " +
                        "currentSize = $currentSize"
            )
            webViewTemp
        }

    @Synchronized
    fun removeWebView(_webView: WebView?) {
        //TODO 是否存在问题未知
        var webView: WebView? = _webView
        if (webView == null) {
            return
        }
        (webView.parent as ViewGroup?)?.removeView(webView)
        inUse.remove(webView)
        if (available.size < maxSize) {
            webView.apply {
                stopLoading()
                loadUrl("about:blank")
                webViewClient = null
                webChromeClient = null
                clearCache(true)
                clearHistory()
                initWebViewSettings(this)
            }
            available.add(webView)
            currentSize--
            logd(
                "执行removeWebView, " +
                        "available.size = ${available.size}, " +
                        "inUse.size = ${inUse.size}, " +
                        "currentSize = $currentSize"
            )
        } else {
            webView = null
            currentSize--
            logd(
                "执行removeWebView, " +
                        "available已满, " +
                        "available.size = ${available.size}, " +
                        "inUse.size = ${inUse.size}, " +
                        "currentSize = $currentSize"
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    @MainThread
    fun onLifecycleOwnerCreate(owner: LifecycleOwner) {
        if (owner !is Context) {
            throw Exception("LifeCycleOwner必须是Context的子类")
        }
        context = owner
        for (i in 0 until maxSize) {
            val webView = WebView(owner)
            initWebView(webView)
            available.add(webView)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @MainThread
    fun onLifecycleOwnerDestroy(owner: LifecycleOwner) {
        inUse.forEach {
            it.stopLoading()
            it.settings.javaScriptEnabled = false
            it.clearCache(true)
            it.clearHistory()
        }
        available.forEach {
            it.stopLoading()
        }
        currentSize = 0
        context = null
    }

    private fun initWebView(webView: WebView) {
        webView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        initWebViewSettings(webView)
        webView.loadUrl("about:blank")
    }

    private fun initWebViewSettings(webView: WebView) {
        webView.apply {
            settings.apply {
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false
                allowFileAccess = false
                defaultTextEncodingName = "utf-8"
                javaScriptEnabled = false
                javaScriptCanOpenWindowsAutomatically = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                blockNetworkImage = false
            }
        }
    }

    private fun logd(s: String) {
        Log.d("WebViewPool", s)
    }
}