package com.sion.zhihudailypurified.view.activity

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityIndexBinding
import com.sion.zhihudailypurified.network.WebViewPool
import com.sion.zhihudailypurified.utils.toast
import com.sion.zhihudailypurified.view.fragment.CommentsFragment
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.GalleryFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment
import com.sion.zhihudailypurified.viewModel.activity.IndexViewModel

class IndexActivity : BaseActivity<ActivityIndexBinding, IndexViewModel>(), LifecycleOwner {

    override fun setLayoutId(): Int {
        return R.layout.activity_index
    }

    override fun setViewModel(): Class<IndexViewModel> {
        return IndexViewModel::class.java
    }

    override fun initView() {
        lifecycle.addObserver(getWebViewPool())
        vm.isOnline.observe(this, Observer {
            if (it) {
                toast("网络已连接")
            } else {
                toast("网络已断开")
            }
        })
        supportFragmentManager.beginTransaction()
            .add(
                R.id.flFragmentContainer,
                StoriesFragment(),
                StoriesFragment.TAG
            )
            .commit()
    }

    override fun initData() {

    }


    fun switchToContent(fragment: StoriesFragment, displayType: Int, initialPos: Int) {
        supportFragmentManager.beginTransaction()
            .hide(fragment)
            .add(
                R.id.flFragmentContainer,
                ContentsDisplayFragment(displayType, initialPos),
                ContentsDisplayFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

    fun switchToComments(
        fragment: ContentsDisplayFragment,
        id: Int,
        commentsNum: Int,
        longCommentsNum: Int,
        shortCommentsNum: Int
    ) {
        supportFragmentManager.beginTransaction()
            .hide(fragment)
            .add(
                R.id.flFragmentContainer,
                CommentsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(CommentsFragment.STORY_ID, id)
                        putInt(CommentsFragment.COMMENTS_NUM, commentsNum)
                        putInt(CommentsFragment.LONG_COMMENTS_NUM, longCommentsNum)
                        putInt(CommentsFragment.SHORT_COMMENTS_NUM, shortCommentsNum)
                    }
                },
                CommentsFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

    fun switchToGallery(
        fragment: ContentsDisplayFragment,
        initialIndex: Int,
        picNum: Int,
        picUrls: Array<String>
    ) {
        supportFragmentManager.beginTransaction()
            .hide(fragment)
            .add(
                R.id.flFragmentContainer,
                GalleryFragment().apply {
                    arguments = Bundle().apply {
                        putInt(GalleryFragment.INITIAL_INDEX, initialIndex)
                        putInt(GalleryFragment.PIC_NUM, picNum)
                        putStringArray(GalleryFragment.PIC_URLS, picUrls)
                    }
                },
                GalleryFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

    fun getWebViewPool(): WebViewPool =
        vm.webViewPool

}
