package com.sion.zhihudailypurified.view

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.App
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.StoriesAdapter
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityIndexBinding
import com.sion.zhihudailypurified.utils.HtmlUtils
import com.sion.zhihudailypurified.utils.logi
import com.sion.zhihudailypurified.viewModel.IndexViewModel

class IndexActivity : BaseActivity<ActivityIndexBinding, IndexViewModel>() {

    private val showAction by lazy {
        TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        ).apply {
            duration = 100
        }
    }
    private val hideAction by lazy {
        TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f
        ).apply {
            duration = 100
        }
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

    private var currentDisplay = LIST

    override fun setLayoutId(): Int {
        return R.layout.activity_index
    }

    override fun setViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun initView() {
        val adapter = StoriesAdapter()
        ui.rvStories.adapter = adapter
        ui.rvStories.layoutManager = LinearLayoutManager(this)
        vm.stories.observe(this, Observer(adapter::submitList))
        vm.updateTopStories.observe(this, Observer {
            if (it) {
                adapter.bannerAdapter!!.notifyDataSetChanged()
            }
        })

        ui.llContent.addView(webView)


        vm.content.observe(this, Observer {
            ui.tvTitle.text = it.title
            ui.tvSubTitle.text = it.image_source
            Glide.with(this@IndexActivity)
                .load(it.image)
                .into(ui.ivImage)

            webView.apply {
                val htmlData = HtmlUtils.createHtmlData(it.body, it.css, it.js)
                loadData(htmlData, "text/html;charset=UTF-8", null)
            }
        })

    }

    override fun initData() {
        vm.obtainTopStories()
    }

    override fun onStart() {
        super.onStart()
        (ui.rvStories.adapter as StoriesAdapter).banner?.start()
    }

    override fun onStop() {
        super.onStop()
        (ui.rvStories.adapter as StoriesAdapter).banner?.stop()
    }

    override fun beforeDestory() {
        webView.let { webView ->
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

    override fun onBackPressed() {
        if (currentDisplay == CONTENT) {
            switchToList()
        } else {
            super.onBackPressed()
        }
    }

    fun switchToList() {
        currentDisplay = LIST
        ui.rvStories.visibility = View.VISIBLE
//        ui.rvStories.startAnimation(showAction)
        ui.svContent.startAnimation(hideAction)
        ui.svContent.visibility = View.GONE
        (ui.rvStories.adapter as StoriesAdapter).banner?.start()
    }

    fun switchToContent(id: Int) {
        (ui.rvStories.adapter as StoriesAdapter).banner?.stop()

        ui.tvTitle.text = ""
        ui.tvSubTitle.text = ""
        ui.ivImage.setImageBitmap(
            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                .apply {
                    eraseColor(Color.parseColor("#FFFFFFFF"))
                })
        webView.apply {
            val htmlData = HtmlUtils.createHtmlData("", emptyList(), emptyList())
            loadData(htmlData, "text/html;charset=UTF-8", null)
        }

        currentDisplay = CONTENT
        ui.svContent.visibility = View.VISIBLE
//        ui.rvStories.startAnimation(hideAction)
        ui.svContent.startAnimation(showAction)
        ui.rvStories.visibility = View.GONE


        vm.obtainStoryContent(id)
    }

    companion object {
        const val LIST = 0
        const val CONTENT = 1
    }
}
