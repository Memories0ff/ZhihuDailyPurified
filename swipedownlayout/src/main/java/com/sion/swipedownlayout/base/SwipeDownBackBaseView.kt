package com.sion.swipedownlayout.base

import android.content.Context
import android.widget.FrameLayout
import com.sion.swipedownlayout.dp

abstract class SwipeDownBackBaseView(context: Context) : FrameLayout(context) {

    init {
        setWillNotDraw(false)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            0.dp
        )
    }

    protected lateinit var backViewParent: BackViewParent
    fun setParent(backViewParent: BackViewParent) {
        this.backViewParent = backViewParent
    }

    /**
     * 由下拉Progress值更新View，由外部直接调用
     * @param progress 范围为[0.0,1.0]，小于0.0大于1.0则强制设为0.0和1.0
     * @param realProgress 当前实际的进度值
     */
    fun <T> updateDraggedView(
        progress: Float,
        realProgress: T
    ) where T : Number, T : Comparable<T> {
        updateDragProgress(progress, realProgress)
        invalidate()
    }

//    /**
//     * 加载的循环动画是否继续循环
//     */
//    protected var isLoadingAnimationActive = false
//
//    /**
//     * 设置加载的循环动画是否继续循环
//     */
//    fun setLoadingAnimationIsFinished() {
//        isLoadingAnimationActive = false
//    }

//    /**
//     * 加载前的动画呈现
//     */
//    open fun fromReleaseToLoadingStartAnimation() {}
//
//    open fun onFromReleaseToLoadingStartAnimationFinish() {}

    /**
     * 更新下拉Progress值
     * 包含下拉、加载的情况从放手后到加载前以及不加载的情况从放手到完全收起的动画
     * 由下拉事件驱动
     * @param progress 起始位0.0，结束为1.0
     * @param realProgress 当前实际的进度值
     */
    protected abstract fun <T> updateDragProgress(
        progress: Float,
        realProgress: T
    ) where T : Number, T : Comparable<T>

    /**
     * 滑入加载阈值时执行的方法
     */
    open fun onEnterLoadingThreshold() {}

    /**
     * 滑出加载阈值时执行的方法
     */
    open fun onExitLoadingThreshold() {}

    /**
     * 开始加载循环动画的操作
     * 结束后如果加载成功执行onLoadingSuccessAnimation，加载失败执行onLoadingFailedAnimation
     */
    open fun onLoadingAnimation() {}

    /**
     * 加载动画结束后开始提示加载<p>完成</p>动画的操作
     * 结束后执行收起下拉菜单的动画
     */
    open fun onLoadingSuccessAnimation() {}

    /**
     * 加载动画结束后开始提示加载<p>失败</p>动画的操作
     * 结束后执行收起下拉菜单的动画
     */
    open fun onLoadingFailedAnimation() {}

    /**
     * 所有动画都结束后的操作
     */
    open fun onAllAnimationFinished() {}

}