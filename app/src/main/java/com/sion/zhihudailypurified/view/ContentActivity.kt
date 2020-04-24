package com.sion.zhihudailypurified.view

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.App
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityContentBinding
import com.sion.zhihudailypurified.utils.HtmlUtils
import com.sion.zhihudailypurified.viewModel.ContentViewModel

class ContentActivity : BaseActivity<ActivityContentBinding, ContentViewModel>() {

    private var webView: WebView? = null

    override fun setLayoutId(): Int =
        R.layout.activity_content

    override fun setViewModel(): ContentViewModel {
        return ContentViewModel(intent.getIntExtra(STORY_ID, -1))
    }

    override fun initView() {
        vm.content.observe(this, Observer {
            ui.tvTitle.text = it.title
            ui.tvSubTitle.text = it.image_source
            Glide.with(this@ContentActivity)
                .load(it.image)
                .into(ui.ivImage)

            WebView(App.getAppContext()).let { webView ->
                webView.settings.apply {
                    setSupportZoom(false)
                    builtInZoomControls = false
                    displayZoomControls = true
                    allowFileAccess = false
                    defaultTextEncodingName = "utf-8"
                }
                webView.apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            view?.loadUrl(url)
                            return true
                        }
                    }
                    val htmlData = HtmlUtils.createHtmlData(it.body, it.css, it.js)
                    loadData(htmlData, "text/html;charset=UTF-8", null)
                }
                ui.llContent.addView(webView)

            }
        })

    }

    override fun initData() {
        vm.obtainStoryContent()
    }

    override fun beforeDestory() {
        webView?.let { webView ->
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            val parent = webView.getParent();
            if (parent != null) {
                (parent as ViewGroup).removeView(webView);
            }

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.settings.javaScriptEnabled = false;
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
    }

    companion object {
        const val STORY_ID = "STORY_ID"
    }

}
