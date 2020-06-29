package com.sion.zhihudailypurified.view.listeners

import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment

class OnStoryPageChangeListener(val fragment: ContentsDisplayFragment) :
    ViewPager.OnPageChangeListener {

    private var isDragPage = false
//    private var canJumpPage = true

    private var isLastPage = false
    private var isArriveThreshold = false
    private var isLoading = false
    private var continueLoadThreshold = 6
    override fun onPageScrollStateChanged(state: Int) {
        isDragPage = (state == ViewPager.SCROLL_STATE_DRAGGING)
        //滚动viewpager更新数据源与列表同步
        //??????????????????????偶尔闪退
//                    if (displayType == STORIES && state == ViewPager.SCROLL_STATE_IDLE) {
//                        ui.vpContents.scrollable = false
//                        adapter!!.notifyDataSetChanged()
//                        ui.vpContents.scrollable = true
//                    }
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
//        if (isArriveThreshold && isLastPage && isDragPage && positionOffsetPixels == 0) {
//            if (canJumpPage) {
//                canJumpPage = false
//                Log.d("addOnPageChangeListener", "正在拖动，可以继续加载")
//            }
//        }
    }

    override fun onPageSelected(position: Int) {
        if (fragment.displayType == ContentsDisplayFragment.STORIES) {
            //滚动viewpager同时更新数据源
//                        ((((activity as IndexActivity).supportFragmentManager.findFragmentByTag(
//                            StoriesFragment.TAG
//                        )) as StoriesFragment).ui.rvStories.adapter as StoriesAdapter).apply {
//                            continueLoad(position)
//                        }
            val storiesFragment =
                ((fragment.activity as IndexActivity).supportFragmentManager.findFragmentByTag(
                    StoriesFragment.TAG
                )) as StoriesFragment
            isLastPage = (position == storiesFragment.vm.stories.value!!.size - 1)
            isArriveThreshold =
                (position >= storiesFragment.vm.stories.value!!.size - 1 - continueLoadThreshold)
            if (continueLoadListener != null && isArriveThreshold && !isLoading) {
                isLoading = true
                Log.d("addOnPageChangeListener", "继续加载")
                continueLoadListener!!.continueLoad(position)
                //加载完毕（可能没有成功）
//                loadingFinish()
            }
            Log.d(
                "addOnPageChangeListener",
                "position:${position}, is arrive threshold:${isArriveThreshold}"
            )
        }
    }

    //在加载过程完毕必须执行
    fun loadingFinish() {
        val storiesFragment =
            ((fragment.activity as IndexActivity).supportFragmentManager.findFragmentByTag(
                StoriesFragment.TAG
            )) as StoriesFragment
        val position = fragment.ui.vpContents.currentItem
        isArriveThreshold =
            (position >= storiesFragment.vm.stories.value!!.size - 1 - continueLoadThreshold)
        isLastPage = (position == storiesFragment.vm.stories.value!!.size - 1)
        isLoading = false
    }

    interface ContinueLoadListener {
        fun continueLoad(position: Int)
    }

    var continueLoadListener: ContinueLoadListener? = null
}