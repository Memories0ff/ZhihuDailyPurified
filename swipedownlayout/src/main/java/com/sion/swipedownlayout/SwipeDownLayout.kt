package com.sion.swipedownlayout

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.view.get
import com.sion.swipedownlayout.base.BackViewParent
import com.sion.swipedownlayout.base.SwipeDownBackBaseView

class SwipeDownLayout : FrameLayout, BackViewParent {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
//        backView = SwipeDownAniBackView(this.context)
        //设置默认的BackView加载效果
        backView = TextBackView(context, this)
        addView(backView, 0)
    }

    //最底层的View，用于绘制下拉效果
    private lateinit var backView: SwipeDownBackBaseView
    fun setBackView(baseBackView: SwipeDownBackBaseView) {
        backView = baseBackView
    }

    private val targetView by lazy {
        this[SCROLLING_CHILD_INDEX]
    }

    //
    private var currentBackViewHeight = 0.dp

    //加载时上方留白高度
    private val maxBackViewHeight = 150.dp

    //松手即加载的留白阈值
    private val loadThresholds = 120.dp


//    //是否为向下滑动
//    private var isSwipeDown = false
//
//    //动画是否结束
//    private var isAnimationFinished = true
//
//    //加载是否结束
//    private var isLoadingFinished = true
//
//    //是否应该进行加载动画
//    private var shouldProcessLoadingAnimation = false

    //重置所有标记变量
//    private fun resetAllFlags() {
//        isAnimationFinished = true
//        isLoadingFinished = true
//        shouldProcessLoadingAnimation = false
//        isSwipeDown = false
//    }
    override fun onFinishInflate() {
        if (this.childCount > DIRECT_SUB_VIEW_COUNT) {
            throw Exception("SwipeDownLayout只能有一个直接子View，您可以使用单独的ViewGroup囊括其中")
        }
        super.onFinishInflate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val backViewHeightSpec =
            MeasureSpec.makeMeasureSpec(currentBackViewHeight, MeasureSpec.EXACTLY)
        measureChildWithMargins(backView, widthMeasureSpec, 0, backViewHeightSpec, 0)
        measureChildWithMargins(targetView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        backView.layout(0.dp, 0.dp, 0.dp + backView.measuredWidth, 0.dp + currentBackViewHeight)
        targetView.layout(
            0.dp,
            0.dp + currentBackViewHeight,
            0.dp + targetView.measuredWidth,
            0.dp + currentBackViewHeight + targetView.measuredHeight
        )
    }

    //----------------------自动滚动处理----------------------------

//    private val scroller by lazy {
//        Scroller(context)
//    }
//
//    override fun computeScroll() {
//        if (scroller.computeScrollOffset()) {
//            scrollTo(scroller.currX, scroller.currY);
//            invalidate();
//        }
//    }

    //----------------------事件处理----------------------------

//    private var downY = 0F
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        var result = false
//        when (event.actionMasked) {
//            ACTION_DOWN -> {
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//            ACTION_UP -> {
//                if (isArrivedThreshold()) {
//                    Log.d(TAG, "准备刷新")
//                } else {
//                    Log.d(TAG, "不刷新，收起")
//                }
//                result = true
//            }
//            ACTION_CANCEL -> {
//            }
//        }
//        return result
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        var result = false
//        when (ev.actionMasked) {
//            ACTION_DOWN -> {
//                downY = ev.y
//            }
//            ACTION_MOVE -> {
//                val dy = ev.y - downY
//                //向下滑动
//                if (!childCanScrollUp() && dy > 0) {
//                    //拦截事件
//                    result = true
//                }
//            }
//            ACTION_UP -> {
//
//            }
//            else -> {
//            }
//        }
//        return result
//    }
    //----------------------嵌套滚动处理----------------------------

    /**
     * 放开手且未完全收起的状态
     */
    private var isReleasedAndNotClosed = false

    /**
     * 是否正在刷新，isReleasedAndNotClosed时间范围包含isRefreshing
     */
    private var isRefreshing = false

    /**
     * 刷新的操作
     */
    private var refreshingListener: (() -> Unit)? = null
    fun setOnRefreshingListener(action: () -> Unit) {
        refreshingListener = action
    }

    private fun refresh() {
        refreshingListener?.invoke()
    }

    /**
     * 刷新结束
     */
    fun refreshingFinish(isLoadingSuccess: Boolean) {
//        TODO 分别展示加载完毕动画和关闭动画
        stopLoadingAnimation(isLoadingSuccess)
    }

    private val nestedScrollingParentHelper by lazy {
        NestedScrollingParentHelper(this)
    }

    override fun onStartNestedScroll(
        child: View?,
        target: View?,
        nestedScrollAxes: Int
    ): Boolean {//1
        return isEnabled && (target is NestedScrollingChild) && (nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0)
    }

    override fun onNestedScrollAccepted(child: View?, target: View?, axes: Int) {//2
        super.onNestedScrollAccepted(child, target, axes)
    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray?) {//3
        //动画和加载执行时不可以通过拖拽改变下拉的大小
        if (!isReleasedAndNotClosed) {
            target?.let {
                //收起刷新界面
                if (it.top > 0 && dy > 0) {
                    val tempTop = it.top - dy
                    it.top = tempTop.coerceAtLeast(0)
                    consumed?.set(1, dy)
                    onDragging()
                }
                //打开刷新界面
                else if (!target.canScrollVertically(-1) && dy < 0) {
                    val realDy =
                        (dy.toFloat() * (1F - it.top.toFloat() / maxBackViewHeight.toFloat())).toInt()
                    val tempTop = it.top - realDy
                    it.top = tempTop.coerceAtMost(maxBackViewHeight)
                    consumed?.set(1, dy)
                    onDragging()
                } else {
                }
            }
        }
        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScroll(
        target: View?,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {//4
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }


    override fun onStopNestedScroll(child: View) {//5
        Log.d(TAG, "NestedScrollingChild 停止滚动")
        if (currentBackViewHeight > 0 && !isReleasedAndNotClosed) {
            isReleasedAndNotClosed = true
            if (isArrivedThreshold()) {
                Log.d(TAG, "刷新")
                //TODO 松手到加载的动画，完毕后再执行后面的操作
                onReleaseToLoadingAnimation()
//                onRefresh()
            } else {
                Log.d(TAG, "不刷新")
                //TODO 关闭下拉的动画
                onClosingAnimation()
            }
        }
        super.onStopNestedScroll(child)
    }

    override fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean {
        if ((target?.top ?: 0 > 0) && !isReleasedAndNotClosed) {
            return true
        }
        return false
    }

//    override fun onNestedFling(
//        target: View?,
//        velocityX: Float,
//        velocityY: Float,
//        consumed: Boolean
//    ): Boolean {
//        return false
//    }

    override fun getNestedScrollAxes(): Int {
        return nestedScrollingParentHelper.nestedScrollAxes
    }

    private fun isArrivedThreshold(): Boolean = currentBackViewHeight > loadThresholds
    fun childCanScrollUp(): Boolean {
        if (targetView.canScrollVertically(-1)) {
            return true
        }
        return false
    }


    /**
     * 拖拽时的动画
     */

    private fun onDragging() {
        currentBackViewHeight = targetView.top
        backView.bottom = currentBackViewHeight
        if (isArrivedThreshold()) {
            backView.onEnterLoadingThreshold()
        } else {
            backView.onExitLoadingThreshold()
        }
        backView.updateDraggedView(
            currentBackViewHeight.toFloat() / loadThresholds.toFloat(),
            currentBackViewHeight
        )
    }

    /**
     * 展示从松开手到开始加载的动画
     */
    private val releaseToLoadingAnimation by lazy {
        ObjectAnimator.ofInt(targetView, "top", currentBackViewHeight, loadThresholds).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            doOnEnd { onShowLoadingAnimation() }
            addUpdateListener { onDragging() }
        }
    }

    private fun onReleaseToLoadingAnimation() {
        isRefreshing = true
        //执行动画
        releaseToLoadingAnimation.setValues(
            PropertyValuesHolder.ofInt(
                "top",
                currentBackViewHeight,
                loadThresholds
            )
        )
        releaseToLoadingAnimation.duration =
            (250F * (currentBackViewHeight - loadThresholds).toFloat() / (maxBackViewHeight - loadThresholds)).toLong()
        releaseToLoadingAnimation.start()
    }

    /**
     * 展示正在加载动画
     */
    private fun onShowLoadingAnimation() {
        backView.onLoadingAnimation()
        refresh()
    }

    /**
     * 停止正在加载的动画
     */

    private fun stopLoadingAnimation(isLoadingSuccess: Boolean) {
        if (isLoadingSuccess) {
            backView.onLoadingSuccessAnimation()
        } else {
            backView.onLoadingFailedAnimation()
        }
    }


    /**
     * 执行收起下拉菜单动画时的操作
     */
    override fun onAfterLoadingFinishAnimation() {
        onClosingAnimation()
    }

    /**
     * 收起动画
     */
    private val closingAnimation by lazy {
        ObjectAnimator.ofInt(targetView, "top", currentBackViewHeight, 0).apply {
            interpolator = DecelerateInterpolator()
            doOnEnd {
                backView.onAllAnimationFinished()
                isRefreshing = false
                isReleasedAndNotClosed = false
                targetView.top = 0
                backView.bottom = 0
                currentBackViewHeight = 0
            }
            addUpdateListener {
                onDragging()
            }
        }
    }

    private fun onClosingAnimation() {
        // 动画操作，后面的操作均要在动画结束后执行
        closingAnimation.setValues(PropertyValuesHolder.ofInt("top", currentBackViewHeight, 0))
        closingAnimation.duration =
            (500F * (currentBackViewHeight.toFloat() / loadThresholds.toFloat())).toLong()
        closingAnimation.start()
    }

    companion object {
        //Log Tag
        const val TAG = "SwipeDownLayout"

        //可滚动子View的Index
        const val SCROLLING_CHILD_INDEX = 1

        //直接子控件总数
        const val DIRECT_SUB_VIEW_COUNT = 2
    }

}