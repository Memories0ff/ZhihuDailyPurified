package com.sion.zhihudailypurified.view.laytoutManager

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//解决首页加载完成后RecyclerView自动滑动问题
class StoriesLayoutManager(context: Context?) : LinearLayoutManager(context) {
    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean
    ): Boolean {
        //这里的child 是整个HeadView 而不是某个具体的editText
        return false
    }

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ): Boolean {
        //这里的child 是整个HeadView 而不是某个具体的editText
        return false
    }
}