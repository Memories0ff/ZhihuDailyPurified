package com.sion.zhihudailypurified.view.fragment

import android.view.ViewGroup
import android.webkit.WebView
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
//            webViewClient = object : WebViewClient() {
//                override fun onReceivedError(
//                    view: WebView?,
//                    request: WebResourceRequest?,
//                    error: WebResourceError?
//                ) {
//                    when (error?.errorCode) {
//                        ERROR_TIMEOUT -> {
//                            toast("加载超时")
//                        }
//                        ERROR_CONNECT -> {
//                            toast("连接不到服务器")
//                        }
//                        ERROR_UNKNOWN -> {
//                            toast("未知错误")
//                        }
//                    }
//                    super.onReceivedError(view, request, error)
//                }
//            }
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