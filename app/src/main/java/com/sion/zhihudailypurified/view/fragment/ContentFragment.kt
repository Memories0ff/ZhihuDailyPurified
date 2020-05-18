package com.sion.zhihudailypurified.view.fragment

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.App
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentBinding
import com.sion.zhihudailypurified.utils.HtmlUtils
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
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(url)
                    return true
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
                vm.updateExtraInfo(it, this@ContentFragment)
            }
            Glide.with(this@ContentFragment)
                .load(it.image)
                .into(ui.ivImage)

            webView.apply {
                val htmlData = HtmlUtils.createHtmlData(it.body, it.css, it.js)
                loadData(htmlData, "text/html;charset=UTF-8", null)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        vm.content.value?.let {
            vm.markRead(displayType, it, this@ContentFragment)
            vm.updateExtraInfo(it, this)
        }
    }

    override fun initData() {
        vm.obtainStoryContent(storyId)
    }

    override fun onDestroy() {
        webView.let { webView ->//?????????????????????????????????????????webview点击后瞬移到顶部，上方图片正好消失，左右滑动显示后直接瞬移
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destroy()
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
        super.onDestroy()
    }

    companion object {
        const val TAG = "CONTENT_FRAGMENT"
        const val STORY_ID = "STORY_ID"
    }

}