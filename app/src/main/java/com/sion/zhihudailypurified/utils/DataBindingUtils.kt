package com.sion.zhihudailypurified.utils

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.App

@BindingAdapter("bind:imageUrl")
fun loadImage(iv: ImageView, url: String) {
    Glide.with(App.getAppContext())
        .load(url)
        .into(iv)
}

@BindingAdapter("bind:webContent")
fun loadWebPage(wv: WebView, content: String) {
    wv.settings.apply {
        setSupportZoom(false)
        builtInZoomControls = false
        displayZoomControls = true
        allowFileAccess = false
        defaultTextEncodingName = "utf-8"
    }
    wv.apply {
//        webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest?
//            ): Boolean {
//                view?.loadUrl(url)
//                return true
//            }
//        }
        loadData(content, "text/html;charset=UTF-8", null)
    }
}