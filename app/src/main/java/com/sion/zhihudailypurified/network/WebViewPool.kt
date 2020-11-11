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

class WebViewPool : LifecycleObserver {
    private val available by lazy {
        ArrayList<WebView>()
    }
    private val inUse by lazy {
        ArrayList<WebView>()
    }

    //    private var context: Context? = null
    private val maxSize = 16

    companion object {
        private val lock = byteArrayOf()

        @Volatile
        private var instance: WebViewPool? = null

        fun getInstance(): WebViewPool {
            if (instance == null) {
                synchronized(WebViewPool::class.java) {
                    if (instance == null) {
                        instance = WebViewPool()
                    }
                }
            }
            return instance!!
        }

    }

    fun getWebView(context: Context): WebView {
        synchronized(lock) {
            if (available.size > 0) {
                val webViewTemp = available[0]
                //防止有未从ViewGroup移出的WebView添加到其他ViewGroup中
                (webViewTemp.parent as ViewGroup?)?.removeView(webViewTemp)
                available.removeAt(0)
                inUse.add(webViewTemp)
                logd(
                    "执行getWebView, " +
                            "available.size = ${available.size}, " +
                            "inUse.size = ${inUse.size}, "
                )
                return webViewTemp
            } else {
                if (context == null) {
                    throw Exception("获取WebView需要Context")
                }
                val webViewTemp = WebView(context)
                initWebView(webViewTemp)
                inUse.add(webViewTemp)
                logd(
                    "执行getWebView, " +
                            "available已空, " +
                            "available.size = ${available.size}, " +
                            "inUse.size = ${inUse.size}, "
                )
                return webViewTemp
            }
        }
    }

    @Synchronized
    fun removeWebView(_webView: WebView?) {
        synchronized(lock) {
            removeWebViewWithoutSync(_webView)
        }
    }

    private fun removeWebViewWithoutSync(_webView: WebView?) {
        //TODO 留意是否存在问题（暂未发现）
        var webView: WebView? = _webView
        if (webView == null) {
            return
        }
        if (!inUse.remove(webView)) {
            return
        }
        (webView.parent as ViewGroup?)?.removeView(webView)
        if (available.size < maxSize) {
            recycleWebView(webView)
            available.add(webView)
            logd(
                "执行removeWebView, " +
                        "available.size = ${available.size}, " +
                        "inUse.size = ${inUse.size}, "
            )
        } else {
            webView.removeAllViews()
            webView.destroy()
            webView = null
            logd(
                "执行removeWebView, " +
                        "available已满, " +
                        "available.size = ${available.size}, " +
                        "inUse.size = ${inUse.size}, "
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    @MainThread
    fun create(owner: LifecycleOwner) {
        synchronized(lock) {
            if (owner !is Context) {
                throw Exception("Context必须实现LifeCycleOwner接口")
            }
            for (i in 0 until maxSize) {
                val webView = WebView(owner)
                initWebView(webView)
                available.add(webView)
            }
        }
    }

    @MainThread
    fun create(context: Context) {
        synchronized(lock) {
            for (i in 0 until maxSize) {
                val webView = WebView(context)
                initWebView(webView)
                available.add(webView)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @MainThread
    fun destroy(owner: LifecycleOwner) {
        synchronized(this) {
            inUse.forEach {
                it.stopLoading()
                it.settings.javaScriptEnabled = false
                it.clearCache(false)
                it.clearHistory()
            }
            available.forEach {
                it.stopLoading()
            }
        }
    }

    @MainThread
    fun destroy() {
        synchronized(this) {
            inUse.forEach {
                it.stopLoading()
                it.settings.javaScriptEnabled = false
                it.clearCache(false)
                it.clearHistory()
            }
            available.forEach {
                it.stopLoading()
            }
        }
    }

    fun cleanCache() {
        synchronized(lock) {
            Log.d("WebViewPool", "CleanCache")
            while (inUse.isNotEmpty()) {
                removeWebViewWithoutSync(inUse.last())
            }
        }
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
                allowFileAccess = false
                allowFileAccessFromFileURLs = false
                allowUniversalAccessFromFileURLs = false
            }
        }
    }

    private fun recycleWebView(webView: WebView) {
        webView.apply {
            stopLoading()
            removeAllViews()
            loadUrl("about:blank")
            webViewClient = null
            webChromeClient = null
            clearCache(false)
            clearHistory()
            initWebViewSettings(this)
        }
    }

    private fun logd(s: String) {
        Log.d("WebViewPool", s)
    }
}