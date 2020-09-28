package com.sion.zhihudailypurified.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentBinding
import com.sion.zhihudailypurified.entity.StoryContentExtraBean
import com.sion.zhihudailypurified.utils.HtmlUtils
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.viewModel.fragment.ContentViewModel

class ContentFragment(private val displayType: Int) :
    BaseFragment<FragmentContentBinding, ContentViewModel>() {

    private val storyId by lazy {
        arguments!!.getInt(STORY_ID)
    }

    private val errorUI by lazy {
        val viewStub = ui.vsContentError.viewStub!!
        viewStub.inflate().findViewById<LinearLayout>(R.id.llClickRetry).apply {
            setOnClickListener {
                retry()
            }
        }
    }

    private val webView: WebView by lazy {
        //TODO 不再使用WebViewPool
        getWebViewPool()
            .getWebView()
            .apply {
                settings.apply {
                    //先加载文字再加载图片
                    blockNetworkImage = true
//                TODO addJavascriptInterface()显示和保存图片界面
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        //开始加载网页时显示ProgressBar
                        uiChangeForLoading()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        //先加载文字再加载图片
                        settings.apply {
                            blockNetworkImage = false
                            if (!loadsImagesAutomatically) {
                                loadsImagesAutomatically = true
                            }
                        }
                        //加载完显示WebView
                        uiChangeForFinish()
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        settings.blockNetworkImage = true
//                        uiChangeForError()
//                        toast("网页加载失败")
                        //TODO 显示错误信息
                        super.onReceivedError(view, request, error)
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
        //额外信息显示重置为0
//        vm.updateExtraInfo(StoryContentExtraBean(0, 0, 0, 0), this@ContentFragment)

        //加载时用ProgressBar代替，加载完成再显示
        uiChangeForLoading()

        ui.llContent.addView(webView)

        vm.content.observe(this, Observer {
            //加载失败传入null
            if (it == null) {
                //内容加载失败的处理
                uiChangeForError()
                return@Observer
            }
            ui.tvTitle.text = it.title
            ui.tvSubTitle.text = it.image_source
            if (this.lifecycle.currentState == Lifecycle.State.RESUMED) {
                //TODO 已读标记在网页加载完进行
                vm.markRead(displayType, it, this@ContentFragment)
            }
            Glide.with(this@ContentFragment)
                .load(it.image)
                .placeholder(R.drawable.ic_baseline_pic_placeholder_96)
                .error(R.drawable.ic_baseline_pic_broken_96)
                .into(ui.ivImage)

            webView.apply {
                val htmlData = HtmlUtils.createHtmlData(it.body, it.css, it.js)
                loadData(htmlData, "text/html;charset=UTF-8", null)
            }
        })
        vm.extra.observe(this, Observer {
            if (this.lifecycle.currentState == Lifecycle.State.RESUMED) {
                vm.updateExtraInfo(it ?: StoryContentExtraBean(0, 0, 0, 0), this@ContentFragment)
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
        vm.extra.value.let {
            vm.updateExtraInfo(it ?: StoryContentExtraBean(0, 0, 0, 0), this)
        }
    }

    override fun initData() {
        vm.obtainStoryContentAndExtra(storyId)
    }

    private fun retry() {
        uiChangeForLoading()
        vm.obtainStoryContentAndExtra(storyId)
    }

    override fun onShow() {
        //TODO 此处添加加载失败后自动重新加载功能

    }

    override fun onDestroy() {
        (requireActivity() as IndexActivity).getWebViewPool().removeWebView(webView)
        super.onDestroy()
    }

    private fun uiChangeForLoading() {
        errorUI.visibility = View.GONE
        webView.visibility = View.GONE
        ui.pbWebViewLoading.visibility = View.VISIBLE
    }

    private fun uiChangeForFinish() {
        errorUI.visibility = View.GONE
        ui.pbWebViewLoading.visibility = View.GONE
        webView.visibility = View.VISIBLE
    }

    private fun uiChangeForError() {
        webView.visibility = View.GONE
        ui.pbWebViewLoading.visibility = View.GONE
        errorUI.visibility = View.VISIBLE
    }

    private fun getWebViewPool() =
        (requireActivity() as IndexActivity).getWebViewPool()

    companion object {
        const val TAG = "CONTENT_FRAGMENT"
        const val STORY_ID = "STORY_ID"
    }

}