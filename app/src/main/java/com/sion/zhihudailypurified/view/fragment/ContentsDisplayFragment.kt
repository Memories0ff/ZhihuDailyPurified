package com.sion.zhihudailypurified.view.fragment

import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.ContentsVPAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentsDisplayBinding
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.viewModel.fragment.ContentsDisplayViewModel

class ContentsDisplayFragment(private val displayType: Int, private val initialPos: Int) :
    BaseFragment<FragmentContentsDisplayBinding, ContentsDisplayViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.fragment_contents_display
    }

    override fun setViewModel(): Class<out ContentsDisplayViewModel> {
        return ContentsDisplayViewModel::class.java
    }

    override fun initView() {
        ui.contentExtraField = ObservableField()
        ui.vpContents.apply {
            adapter = ContentsVPAdapter(displayType, activity as FragmentActivity)
            offscreenPageLimit = 1
            currentItem = initialPos
        }
        ui.llBtnComments.setOnClickListener {
            (activity as IndexActivity).switchToComments(
                this,
                (activity!!.supportFragmentManager.findFragmentByTag(StoriesFragment.TAG) as StoriesFragment).vm.stories.value!![ui.vpContents.currentItem]!!.id
            )
        }
    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        //????????????????????????????????????????普通新闻退出内容界面后列表滚动到此新闻的位置
        if (displayType == STORIES) {
            ((activity as FragmentActivity).supportFragmentManager.findFragmentByTag(StoriesFragment.TAG) as StoriesFragment).vm.lastPos.value =
                ui.vpContents.currentItem
        }
    }

    companion object {
        const val TAG = "CONTENTS_DISPLAY_FRAGMENT"
        const val TOP_STORIES = -1
        const val STORIES = -2
    }

}