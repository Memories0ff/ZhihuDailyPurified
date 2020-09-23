package com.sion.zhihudailypurified.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.App
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentBinding
import com.sion.zhihudailypurified.utils.HtmlUtils
import com.sion.zhihudailypurified.utils.toast
import com.sion.zhihudailypurified.viewModel.fragment.ContentViewModel

class ContentFragment(private val displayType: Int) :
    BaseFragment<FragmentContentBinding, ContentViewModel>() {

    private val storyId by lazy {
        arguments!!.getInt(STORY_ID)
    }

    private val webView: WebView by lazy {
        WebView(App.getAppContext()).apply {
            settings.apply {
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = true
                allowFileAccess = false
                defaultTextEncodingName = "utf-8"
                javaScriptCanOpenWindowsAutomatically = false
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
//                TODO addJavascriptInterface()显示和保存图片
            }
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                }

                override fun onPageFinished(view: WebView?, url: String?) {

                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    //TODO 跳转到知乎app
                    //TODO 先检查是否有知乎app再打开
                    if (request != null) {
                        var url = request.url.toString()
                        url = when {
                            url.startsWith("https://www.", true) -> {
                                url.replace("https://www.zhihu.com/", "", true)
                            }
                            url.startsWith("http://www.", true) -> {
                                url.replace("http://www.zhihu.com/", "", true)
                            }
                            else -> {
                                return true
                            }
                        }
                        url = String.format(
                            "%s$url%s",
                            "intent://",
                            "/#Intent;scheme=zhihu;package=com.zhihu.android;end"
                        )
                        val intent = Intent(url)
                        if (intent.resolveActivity(context.packageManager) != null) {
                            startActivity(intent)
                        } else {
                            //TODO 弹窗请求下载知乎
                            Log.d("shouldOverrideUrl", "下载知乎")
                        }
                    }
                    return true
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    //TODO 显示错误信息
                    super.onReceivedError(view, request, error)
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {

                }
            }
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_content
    }

    override fun setViewModel(): Class<ContentViewModel> {
        return ContentViewModel::class.java
    }

    override fun initView() {
        ui.llContent.addView(webView)


        vm.content.observe(this, Observer {
            ui.tvTitle.text = it.title
            ui.tvSubTitle.text = it.image_source
            if (this.lifecycle.currentState == Lifecycle.State.RESUMED) {
                vm.markRead(displayType, it, this@ContentFragment)
            }
            Glide.with(this@ContentFragment)
                .load(it.image)
                .into(ui.ivImage)

            webView.apply {
                val htmlData = HtmlUtils.createHtmlData(it.body, it.css, it.js)
                loadData(htmlData, "text/html;charset=UTF-8", null)
            }
        })
        vm.extra.observe(this, Observer {
            if (this.lifecycle.currentState == Lifecycle.State.RESUMED) {
                vm.updateExtraInfo(it, this@ContentFragment)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //通过左右滑动浏览的新闻标记为已读
        vm.content.value?.let {
            vm.markRead(displayType, it, this@ContentFragment)
        }
        //更新底部点赞评论数信息
        vm.extra.value?.let {
            vm.updateExtraInfo(it, this)
        }
    }

    override fun initData() {
        vm.obtainStoryContentAndExtra(storyId)
    }

    override fun onDestroy() {
        webView.let { webView ->
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destroy()
            val parent = webView.parent;
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
        super.onDestroy()
    }

    companion object {
        const val TAG = "CONTENT_FRAGMENT"
        const val STORY_ID = "STORY_ID"
    }

}