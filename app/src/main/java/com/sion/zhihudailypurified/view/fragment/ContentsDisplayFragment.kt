package com.sion.zhihudailypurified.view.fragment

import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.ContentsVPAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentContentsDisplayBinding
import com.sion.zhihudailypurified.sharedPreference.spPutBoolean
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
        ui.vpContents.apply {
            adapter = ContentsVPAdapter(displayType, activity as FragmentActivity)
            offscreenPageLimit = 1
            currentItem = initialPos
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    //浏览的不是头条的情况下，执行标记已读操作
                    if (displayType != STORIES) {
                        return
                    }
                    (activity!!.supportFragmentManager.findFragmentByTag(
                        StoriesFragment.TAG
                    ) as StoriesFragment).vm.stories.value!![position]!!.apply {
                        //应该在进入fragment并显示后执行此操作，否则左右滑动不会标记或标记上预加载的未读的新闻
                        if (!isRead.get()!!) {
                            isRead.set(true)
                            spPutBoolean(id.toString(), true, activity!!)
                        }
                    }
                }
            })
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