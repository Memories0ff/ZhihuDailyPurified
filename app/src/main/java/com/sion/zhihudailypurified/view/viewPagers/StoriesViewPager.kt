package com.sion.zhihudailypurified.view.viewPagers

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class StoriesViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    //加载中设为false
    var scrollable = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean =
        if (scrollable) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }

    override fun onTouchEvent(ev: MotionEvent?): Boolean =
        if (scrollable) {
            super.onTouchEvent(ev)
        } else {
            false
        }

}