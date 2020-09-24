package com.sion.zhihudailypurified.viewModel.activity

import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.network.WebViewPool

class IndexViewModel : BaseViewModel() {
    //WebView池
    val webViewPool by lazy {
        WebViewPool.getInstance()
    }
}