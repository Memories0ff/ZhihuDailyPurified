package com.sion.zhihudailypurified.view.fragment

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentsDisplayBinding
import com.sion.zhihudailypurified.utils.toast
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.view.adapter.ContentsVPAdapter
import com.sion.zhihudailypurified.view.adapter.StoriesAdapter
import com.sion.zhihudailypurified.viewModel.fragment.ContentsDisplayViewModel

/**
 * 此页面显示来自top和stories的新闻，
 * 对于某些相同的操作
 * (如标记为已阅读、
 * 退出后已阅读的条目立即滚动到最上方)
 * 实现不相同，需要区分
 */

class ContentsDisplayFragment(val displayType: Int, private val initialPos: Int) :
    BaseFragment<FragmentContentsDisplayBinding, ContentsDisplayViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.fragment_contents_display
    }

    override fun setViewModel(): Class<out ContentsDisplayViewModel> {
        return ContentsDisplayViewModel::class.java
    }

    override fun initView() {
        ui.btnBackToStories.setOnClickListener {
            back()
        }
        (requireActivity() as IndexActivity).vm.isOnline.observe(this, Observer {
            //可以联网时才可左右滑动
            ui.vpContents.isUserInputEnabled = it
        })
        //用于更新额外信息，立即在ui响应
        ui.contentExtraField = vm.contentExtraField
        //设置viewpager
        ui.vpContents.apply {
            adapter = ContentsVPAdapter(
                displayType,
                activity as FragmentActivity
            )
            offscreenPageLimit = 1
            setCurrentItem(initialPos, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    //限于stories
                    if (displayType == STORIES) {
                        //滚动viewpager同时更新数据源
                        ((((activity as IndexActivity).supportFragmentManager.findFragmentByTag(
                            StoriesFragment.TAG
                        )) as StoriesFragment).ui.rvStories.adapter as StoriesAdapter).apply {
                            continueLoad(position)
                        }
                    }
                }
            })
        }
        //进入评论按钮
        ui.llBtnComments.setOnClickListener {
            if (isOnline() == true) {
                (activity as IndexActivity).switchToComments(
                    this,
                    (requireActivity()
                        .supportFragmentManager
                        .findFragmentByTag(StoriesFragment.TAG) as StoriesFragment)
                        .vm.let {
                            when (displayType) {
                                STORIES -> it.stories.value!![ui.vpContents.currentItem]!!.id
                                else -> it.topStories.value!![ui.vpContents.currentItem]!!.id
                            }
                        },
                    ui.contentExtraField!!.get()!!.comments,
                    ui.contentExtraField!!.get()!!.long_comments,
                    ui.contentExtraField!!.get()!!.short_comments
                )
            } else {
                toast(resources.getText(R.string.retry_after_resume_connection).toString())
            }
        }
    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO 普通新闻退出内容界面后列表滚动到此新闻的位置，实现不完美
        //限于stories
        if (displayType == STORIES) {
            ((activity as FragmentActivity).supportFragmentManager.findFragmentByTag(StoriesFragment.TAG) as StoriesFragment).vm.lastPos.value =
                ui.vpContents.currentItem
        }
    }

    private fun isOnline() = (requireActivity() as IndexActivity).vm.isOnline.value

    companion object {
        const val TAG = "CONTENTS_DISPLAY_FRAGMENT"
        const val TOP_STORIES = -1
        const val STORIES = -2
    }

}