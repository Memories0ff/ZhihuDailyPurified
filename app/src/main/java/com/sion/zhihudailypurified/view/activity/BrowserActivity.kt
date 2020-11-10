package com.sion.zhihudailypurified.view.activity

import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.*
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityBrowserBinding
import com.sion.zhihudailypurified.utils.toast
import com.sion.zhihudailypurified.viewModel.activity.BrowserViewModel

class BrowserActivity : BaseActivity<ActivityBrowserBinding, BrowserViewModel>() {

    private val url by lazy {
        intent.getStringExtra(URL)
    }

    private val webView by lazy {
        vm.getWebViewPool().getWebView(this).apply {
            settings.apply {
                javaScriptEnabled = true
            }
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    //标题更新到TitleBar
                    supportActionBar?.title = title
                    super.onReceivedTitle(view, title)
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress == 100) {
                        ui.pbPageLoading.visibility = View.GONE
                    } else {
                        ui.pbPageLoading.apply {
                            visibility = View.VISIBLE
                            progress = newProgress
                        }
                    }
                    super.onProgressChanged(view, newProgress)
                }
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (request?.url.toString().contains("zhihu://")) {
                        //跳转到知乎应用
                        if (isInstalledZhihu()) {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(request?.url.toString()))
                            startActivity(intent)
                        }
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    toast(error.toString())
                    super.onReceivedError(view, request, error)
                }
            }
        }
    }

    override fun setLayoutId(): Int = R.layout.activity_browser

    override fun setViewModel(): Class<out BrowserViewModel> = BrowserViewModel::class.java

    override fun initView() {
        supportActionBar?.let {
            it.title = "正在跳转..."
            it.setHomeAsUpIndicator(R.drawable.ic_round_close_30)
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        ui.flWebViewContainer.addView(webView)
    }

    override fun initData() {
        webView.loadUrl(url)
    }

    override fun onDestroy() {
        vm.getWebViewPool().removeWebView(webView)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            if (webView.copyBackForwardList().currentIndex == 0) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            R.id.runInBrowser -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webView.originalUrl))
                startActivity(intent)
            }
            R.id.refresh -> {
                webView.reload()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.browser_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun isInstalledZhihu(): Boolean {
        packageManager.getInstalledPackages(0).forEach {
            if (it.packageName == "com.zhihu.android") {
                return true
            }
        }
        return false
    }

    companion object {
        const val URL = "URL"
    }
}