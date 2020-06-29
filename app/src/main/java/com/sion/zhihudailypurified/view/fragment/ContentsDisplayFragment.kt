package com.sion.zhihudailypurified.view.fragment

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.ContentsVPAdapter
import com.sion.zhihudailypurified.adapter.StoriesAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentsDisplayBinding
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.viewModel.fragment.ContentsDisplayViewModel

class ContentsDisplayFragment(val displayType: Int, private val initialPos: Int) :
    BaseFragment<FragmentContentsDisplayBinding, ContentsDisplayViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.fragment_contents_display
    }

    override fun setViewModel(): Class<out ContentsDisplayViewModel> {
        return ContentsDisplayViewModel::class.java
    }

    override fun initView() {
        ui.contentExtraField = vm.contentExtraField
        ui.vpContents.apply {
            adapter = ContentsVPAdapter(displayType, activity as FragmentActivity)
            offscreenPageLimit = 1
            currentItem = initialPos
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    //滚动viewpager更新数据源与列表同步
//                    if (state == ViewPager.SCROLL_STATE_IDLE) {
//                        adapter!!.notifyDataSetChanged()
//                        Log.d("addOnPageChangeListener", "刷新数据源2")
//                    }
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (displayType == STORIES) {
                        //滚动viewpager同时更新数据源
                        ((((activity as IndexActivity).supportFragmentManager.findFragmentByTag(
                            StoriesFragment.TAG
                        )) as StoriesFragment).ui.rvStories.adapter as StoriesAdapter).apply {
                            continueLoad(position)
                        }
                        Log.d(
                            "addOnPageChangeListener", "position:${position}, all:${
                            ((activity as IndexActivity).supportFragmentManager.findFragmentByTag(
                                StoriesFragment.TAG
                            ) as StoriesFragment).vm.stories.value!!.size
                            }"
                        )
                    }
                }
            })
        }
//        ui.vpContents.apply {
//            adapter = ContentsVPAdapter(displayType, activity as FragmentActivity)
//            offscreenPageLimit = 1
//            currentItem = initialPos
//            addOnPageChangeListener(OnStoryPageChangeListener(this@ContentsDisplayFragment).apply {
//                continueLoadListener = object : OnStoryPageChangeListener.ContinueLoadListener {
//                    override fun continueLoad(position: Int) {
//                        val storiesFragment =
//                            ((fragment.activity as IndexActivity).supportFragmentManager.findFragmentByTag(
//                                StoriesFragment.TAG
//                            )) as StoriesFragment
//                        (storiesFragment.ui.rvStories.adapter as StoriesAdapter).apply {
//                            continueLoad(position)
//                        }
//                        loadingFinish()
//                    }
//                }
//            })
//        }
        ui.llBtnComments.setOnClickListener {
            (activity as IndexActivity).switchToComments(
                this,
                (activity!!.supportFragmentManager.findFragmentByTag(StoriesFragment.TAG) as StoriesFragment).vm.let {
                    when (displayType) {
                        STORIES -> it.stories.value!![ui.vpContents.currentItem]!!.id
                        else -> it.topStories.value!![ui.vpContents.currentItem]!!.id
                    }
                },
                ui.contentExtraField!!.get()!!.comments,
                ui.contentExtraField!!.get()!!.long_comments,
                ui.contentExtraField!!.get()!!.short_comments
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