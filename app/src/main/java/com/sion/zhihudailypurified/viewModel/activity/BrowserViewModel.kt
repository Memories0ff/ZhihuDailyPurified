package com.sion.zhihudailypurified.viewModel.activity

import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.network.WebViewPool

class BrowserViewModel : BaseViewModel() {
    fun getWebViewPool() = WebViewPool.getInstance()
}