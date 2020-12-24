package com.sion.swipedownlayout

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.graphics.toColorInt
import com.sion.swipedownlayout.base.BackViewParent
import com.sion.swipedownlayout.base.SwipeDownBackBaseView
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class TextBackView(context: Context, backViewParent: BackViewParent) :
    SwipeDownBackBaseView(context) {
    init {
//        setBackgroundColor("#29ACFF".toColorInt())
        setParent(backViewParent)
    }

    /**
     * 是否加载完成
     */
    private var isLoadingSuccess = false

    /**
     * 绘图的阶段
     * 加载动画前中后
     */
    private var drawPhase = Phase.BEFORE_LOADING

    /**
     * 进度值
     */
    private var dragProgress = 0F

    /**
     * 已下拉的高度
     */
    private var swipedHeight = 0


    /**
     * 绘制字符的Paint
     */
    private val textPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 16F.sp
            color = "#13327F".toColorInt()
            strokeWidth = 3F
        }
    }

    override fun onDraw(canvas: Canvas) {
        when (drawPhase) {
            Phase.BEFORE_LOADING -> {
                drawBeforeLoading(canvas)
            }
            Phase.LOADING -> {
                drawLoading(canvas)
            }
            Phase.AFTER_LOADING -> {
                drawAfterLoading(canvas)
            }
        }
    }

    override fun <T> updateDragProgress(
        progress: Float,
        realProgress: T
    ) where T : Number, T : Comparable<T> {
        dragProgress = progress
        swipedHeight = realProgress as Int
    }


    /**
     * 是否下拉到达加载阈值
     */
    private var isOverLoadingThreshHold = false

    /**
     * 滑入加载阈值时执行的方法
     */
    override fun onEnterLoadingThreshold() {
        isOverLoadingThreshHold = true
    }

    /**
     * 滑出加载阈值时执行的方法
     */
    override fun onExitLoadingThreshold() {
        isOverLoadingThreshHold = false
    }


    private var textPaintAlphaSave = 0      //保存再动画执行过程中被修改的透明度值
    private var fraction = 0F               //用于加载过程中字符透明度循环变化动画的变量
        set(value) {
            field = value
            textPaint.alpha = (255F * fraction).roundToInt()
            invalidate()
        }
    private val loadingAnimation by lazy {
        ObjectAnimator.ofFloat(this, "fraction", 1F, 0F, 1F).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            doOnStart {
                textPaintAlphaSave = textPaint.alpha
            }
            doOnCancel {
                textPaint.alpha = textPaintAlphaSave
            }
        }
    }

    /**
     * 开始Loading动画的操作
     */
    override fun onLoadingAnimation() {
        drawPhase = Phase.LOADING
        loadingAnimation.start()
    }

    /**
     * 加载结束的提示动画
     * 提示动画结束后一定要执行{@link BackViewParent#onAfterLoadingFinishAnimation}方法
     * 通知容器BackView的动画已经全部完成
     * 否则提示动画完成后父容器无法收起下拉区域
     */
    private val loadingFinishAnimation by lazy {
        ValueAnimator.ofFloat(0F).apply {
            duration = 500
            doOnEnd {
                backViewParent.onAfterLoadingFinishAnimation()
            }
            addUpdateListener {
                invalidate()
            }
        }
    }

    /**
     * 开始提示加载成功动画的操作
     */
    override fun onLoadingSuccessAnimation() {
        loadingAnimation.cancel()
        isLoadingSuccess = true
        drawPhase = Phase.AFTER_LOADING
        loadingFinishAnimation.start()
    }

    /**
     * 开始提示加载失败动画的操作
     */
    override fun onLoadingFailedAnimation() {
        loadingAnimation.cancel()
        isLoadingSuccess = false
        drawPhase = Phase.AFTER_LOADING
        loadingFinishAnimation.start()
    }

    /**
     * 所有动画都结束后的操作
     */
    override fun onAllAnimationFinished() {
        drawPhase = Phase.BEFORE_LOADING
    }

    /*----------------------------------------不同阶段的动画绘制----------------------------------------*/
    /**
     * 绘制加载前的动画
     */
    private fun drawBeforeLoading(canvas: Canvas) {
        //过阈值显示“下拉加载”，否则显示“松开加载”
        if (!isOverLoadingThreshHold) {
            canvas.drawText(
                beforeLoadingText,
                (width - beforeLoadingTextWidth) / 2,
                (swipedHeight - beforeLoadingTextHeight) / 2 + beforeLoadingTextHeight * 4 / 5,
                textPaint
            )
        } else {
            canvas.drawText(
                readyLoadingText,
                (width - readyLoadingTextWidth) / 2,
                (swipedHeight - readyLoadingTextHeight) / 2 + readyLoadingTextHeight * 4 / 5,
                textPaint
            )
        }
    }

    /**
     * 绘制加载中的动画
     */
    private fun drawLoading(canvas: Canvas) {
        canvas.drawText(
            loadingText,
            (width - loadingTextWidth) / 2,
            (swipedHeight - loadingTextHeight) / 2 + loadingTextHeight * 4 / 5,
            textPaint
        )
    }

    /**
     * 绘制加载后的动画
     */
    private fun drawAfterLoading(canvas: Canvas) {
        if (isLoadingSuccess) {
            canvas.drawText(
                loadingSuccessText,
                (width - loadingSuccessTextWidth) / 2,
                (swipedHeight - loadingSuccessTextHeight) / 2 + loadingSuccessTextHeight * 4 / 5,
                textPaint
            )
        } else {
            canvas.drawText(
                loadingFailedText,
                (width - loadingFailedTextWidth) / 2,
                (swipedHeight - loadingFailedTextHeight) / 2 + loadingFailedTextHeight * 4 / 5,
                textPaint
            )
        }
    }


    /*----------------------------------------字符的测量----------------------------------------*/
    /**
     * 计算“下拉刷新”长宽
     */
    private val beforeLoadingText = "下拉刷新"
    private val beforeLoadingTextBounds by lazy {
        Rect().apply {
            textPaint.getTextBounds(beforeLoadingText, 0, beforeLoadingText.length, this)
        }
    }
    private val beforeLoadingTextWidth by lazy {
        (beforeLoadingTextBounds.right - beforeLoadingTextBounds.left).toFloat()
    }
    private val beforeLoadingTextHeight by lazy {
        (beforeLoadingTextBounds.bottom - beforeLoadingTextBounds.top).toFloat()
    }


    /**
     * 计算“松开刷新”长宽
     */
    private val readyLoadingText = "松开刷新"
    private val readyLoadingTextBounds by lazy {
        Rect().apply {
            textPaint.getTextBounds(readyLoadingText, 0, readyLoadingText.length, this)
        }
    }
    private val readyLoadingTextWidth by lazy {
        (readyLoadingTextBounds.right - readyLoadingTextBounds.left).toFloat()
    }
    private val readyLoadingTextHeight by lazy {
        (readyLoadingTextBounds.bottom - readyLoadingTextBounds.top).toFloat()
    }


    /**
     * 计算“正在加载...”长宽
     */
    private val loadingText = "正在加载..."
    private val loadingTextBounds by lazy {
        Rect().apply {
            textPaint.getTextBounds(loadingText, 0, loadingText.length, this)
        }
    }
    private val loadingTextWidth by lazy {
        (loadingTextBounds.right - loadingTextBounds.left).toFloat()
    }
    private val loadingTextHeight by lazy {
        (loadingTextBounds.bottom - loadingTextBounds.top).toFloat()
    }

    /**
     * 计算“加载完成”长宽
     */
    private val loadingSuccessText = "加载完成"
    private val loadingSuccessTextBounds by lazy {
        Rect().apply {
            textPaint.getTextBounds(loadingSuccessText, 0, loadingSuccessText.length, this)
        }
    }
    private val loadingSuccessTextWidth by lazy {
        (loadingSuccessTextBounds.right - loadingSuccessTextBounds.left).toFloat()
    }
    private val loadingSuccessTextHeight by lazy {
        (loadingSuccessTextBounds.bottom - loadingSuccessTextBounds.top).toFloat()
    }

    /**
     * 计算“加载失败”长宽
     */
    private val loadingFailedText = "加载失败"
    private val loadingFailedTextBounds by lazy {
        Rect().apply {
            textPaint.getTextBounds(loadingFailedText, 0, loadingFailedText.length, this)
        }
    }
    private val loadingFailedTextWidth by lazy {
        (loadingFailedTextBounds.right - loadingFailedTextBounds.left).toFloat()
    }
    private val loadingFailedTextHeight by lazy {
        (loadingFailedTextBounds.bottom - loadingFailedTextBounds.top).toFloat()
    }

    /*----------------------------------------动画阶段----------------------------------------*/
    /**
     * 加载前中后
     */
    enum class Phase {
        BEFORE_LOADING, LOADING, AFTER_LOADING
    }

}