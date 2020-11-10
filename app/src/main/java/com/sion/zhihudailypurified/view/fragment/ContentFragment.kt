package com.sion.zhihudailypurified.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.maning.imagebrowserlibrary.MNImageBrowser
import com.maning.imagebrowserlibrary.model.ImageBrowserConfig
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentBinding
import com.sion.zhihudailypurified.entity.StoryContentExtraBean
import com.sion.zhihudailypurified.utils.HtmlUtils
import com.sion.zhihudailypurified.utils.toast
import com.sion.zhihudailypurified.view.activity.BrowserActivity
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.viewModel.fragment.ContentViewModel
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.gallery_shade.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        getWebViewPool()
            .getWebView(requireContext())
            .apply {
                settings.apply {
                    //先加载文字再加载图片
                    blockNetworkImage = true
                    //启用JS，与原生交互
                    javaScriptEnabled = true
                }
                addJavascriptInterface(JSInterface(), "JSInterface")
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
                        //注入图片点击事件
                        view?.let { addImageClickListener(it) }
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
                        return if (request != null) {
                            //跳转到BrowserActivity
                            val intent = Intent(
                                (activity as IndexActivity),
                                BrowserActivity::class.java
                            ).apply {
                                putExtra(BrowserActivity.URL, request.url.toString())
                            }
                            startActivity(intent)
                            true
                        } else {
                            false
                        }
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

        vm.content.observe(viewLifecycleOwner, Observer {
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
        vm.extra.observe(viewLifecycleOwner, Observer {
            if (this.lifecycle.currentState == Lifecycle.State.RESUMED) {
                vm.updateExtraInfo(it ?: StoryContentExtraBean(0, 0, 0, 0), this@ContentFragment)
                vm.updateCollectionInfo(storyId, this@ContentFragment)
                vm.updateLikeInfo(storyId, this@ContentFragment)
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
            vm.updateCollectionInfo(storyId, this)
            vm.updateLikeInfo(storyId, this)
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

    /**
     * JS-原生交互
     */
    // 注入js函数监听
    private fun addImageClickListener(webView: WebView) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，
        //函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl(
            "javascript:(function () {\n" +
                    "        const contentImgs = document.getElementsByClassName('content-image');\n" +
                    "        const needImgs = [];\n" +
                    "        const srcs = [];\n" +
                    "        for (let i = 0; i < contentImgs.length; i++) {\n" +
                    "            if (contentImgs[i].getAttribute('src').indexOf('https://www.zhihu.com/equation?tex=') === -1) {\n" +
                    "                needImgs.push(contentImgs[i]);\n" +
                    "                srcs.push(contentImgs[i].getAttribute('src'))\n" +
                    "            }\n" +
                    "        }\n" +
                    "        for (let j = 0; j < needImgs.length; j++) {\n" +
                    "            needImgs[j].onclick = function () {\n" +
                    "                javascript:JSInterface.openImagesGallery(j,srcs.length,srcs);\n" +
                    "            }\n" +
                    "        }\n" +
                    "    })();"
        )
    }

    inner class JSInterface {

        /**
         * 点击网页图片打开图片浏览器
         * @param initialIndex 打开的是第几张图片
         * @param picNum 图片个数
         * @param picUrls 所有图片的URL
         */
        @JavascriptInterface
        fun openImagesGallery(initialIndex: Int, picNum: Int, picUrls: Array<String>) {
//            val activity: IndexActivity = this@ContentFragment.activity as IndexActivity
//            activity.switchToGallery(
//                activity.supportFragmentManager.findFragmentByTag(ContentsDisplayFragment.TAG) as ContentsDisplayFragment,
//                initialIndex,
//                picNum,
//                picUrls
//            )
            val shadeView =
                LayoutInflater.from(requireContext()).inflate(R.layout.gallery_shade, null)
            shadeView.tvNumIndication.text =
                getString(R.string.images_gallery_indicator, initialIndex + 1, picNum)
            shadeView.ivBtnDownloadPic.setOnClickListener {
                //保存图片
                savePic(MNImageBrowser.getCurrentImageView().drawable.toBitmap())
            }
            MNImageBrowser.with(requireContext())
                .setTransformType(ImageBrowserConfig.TransformType.Transform_Default)
                .setIndicatorHide(false)
                .setCustomShadeView(shadeView)
                .setCurrentPosition(initialIndex)
                .setImageEngine { context, url, imageView, progressView, customImageView ->
                    Glide.with(context)
                        .load(url)
                        .fitCenter()
                        .placeholder(R.drawable.ic_baseline_pic_placeholder_96)
                        .error(R.drawable.ic_baseline_pic_broken_96)
                        .into(imageView)
                }
                .setImageList(picUrls.toCollection(arrayListOf()))
                .setScreenOrientationType(ImageBrowserConfig.ScreenOrientationType.ScreenOrientation_Portrait)
                .setOnPageChangeListener {
                    shadeView.tvNumIndication.text =
                        getString(R.string.images_gallery_indicator, it + 1, picNum)
                }
                .setOpenPullDownGestureEffect(true)
                .setActivityOpenAnime(R.anim.activity_in)
                .setActivityExitAnime(R.anim.activity_out)
                .show(webView)
        }

        @JavascriptInterface
        fun showToast(s: String) {
            toast(s)
        }

    }

    fun savePic(bitmap: Bitmap) {
        AndPermission
            .with(requireContext())
            .runtime()
            .permission(Permission.WRITE_EXTERNAL_STORAGE)
            .onGranted {
                GlobalScope.launch(Dispatchers.Main) {
                    if (vm.savePic(bitmap, requireContext().contentResolver)) {
                        toast("保存成功")
                    } else {
                        toast("保存失败")
                    }
                }
            }
            .onDenied {
                toast("保存失败，请授予文件写入权限")
            }
            .start()
    }

    companion object {
        const val TAG = "CONTENT_FRAGMENT"
        const val STORY_ID = "STORY_ID"
    }

}