package com.sion.banner

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager.widget.ViewPager
import com.rd.PageIndicatorView
import java.util.*

class Banner(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    LifecycleObserver {

    private var fragment: Fragment? = null
    private var activity: Activity? = null
    private var period: Long = 5000  //ms

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.banner, this, false)
        addView(view)
    }

    /**
     * 设置activity，不观察activity生命周期、不通过监听activity生命周期开关定时滚动功能使用
     */
    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    /**
     * 设置fragment，不观察activity生命周期、不通过监听activity生命周期开关定时滚动功能使用
     */
    fun setFragment(fragment: Fragment) {
        this.fragment = fragment
        this.activity = fragment.activity
    }

    /**
     * 设置观察fragment，通过监听fragment生命周期开关定时滚动功能
     */
    fun observeFragment(fragment: Fragment) {
        if (this.activity == null) {
            this.activity = fragment.activity
            fragment.lifecycle.addObserver(this)
        }
    }

    /**
     * 设置观察activity，通过监听activity生命周期开关定时滚动功能
     */
    fun observeActivity(activity: FragmentActivity) {
        if (this.activity == null) {
            this.activity = activity
            activity.lifecycle.addObserver(this)
        }
    }

    /**
     * 轮播图
     */
    private val bannerPager by lazy {
        findViewById<BannerPager>(R.id.bp).apply {
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    indicator.onPageScrollStateChanged(state)
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    indicator.onPageScrolled(
                        adapter!!.getRealPosition(position),
                        positionOffset,
                        positionOffsetPixels
                    )
                }

                override fun onPageSelected(position: Int) {
                    indicator.onPageSelected(adapter!!.getRealPosition(position))
                }
            })
        }
    }

    /**
     * 指示器
     */
    private val indicator by lazy {
        findViewById<PageIndicatorView>(R.id.indicator)
    }

    /**
     * 占位符
     */
    private val placeholder by lazy {
        findViewById<ProgressBar>(R.id.placeholder)
    }

    /**
     * 轮播图adapter
     */
    private var adapter: BannerAdapter? = null

    fun setAdapter(adapter: BannerAdapter) {
        this.adapter = adapter
        bannerPager.adapter = adapter//TODO 内部存在除0错误(是否解决未知)
        indicator.count = adapter.getRealCount()

    }

    fun getAdapter(): BannerAdapter? {
        return adapter
    }

    /**
     * 跳转到某个轮播图
     * @param realPosition 数据源中的位置
     */
    @Synchronized
    fun setCurrentPosition(realPosition: Int) {
        adapter?.let {
            if (it.getRealCount() > 0) {
                bannerPager.currentItem = realPosition
                indicator.selection = realPosition
            }
        }
    }

    /**
     * 获取当前轮播图显示内容在数据源中的位置
     */
    fun getCurrentPosition(): Int {
        return bannerPager.currentItem % adapter!!.getRealCount()
    }

    /**
     * 定时滚动轮播图
     */
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    /**
     * 开启定时滚动
     */
    @Synchronized
    fun startRolling() {
        if (timer == null) {
            timer = Timer()
        }
        if (timerTask == null) {
            timerTask = object : TimerTask() {
                override fun run() {
                    adapter?.let {
                        activity?.runOnUiThread {
                            setCurrentPosition((bannerPager.currentItem + 1) % it.getRealCount())
                        }
                    }
                }
            }
        }
        //TODO java.lang.IllegalStateException: Task already scheduled or cancelled
        timer!!.schedule(timerTask, period, period)
    }

    /**
     * 关闭定时滚动
     */
    @Synchronized
    fun stopRolling() {
        timer?.let {
            it.cancel()
            timer = null
        }
        timerTask?.let {
            it.cancel()
            timerTask = null
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(owner: LifecycleOwner) {
        Log.d("Banner", "onResume")
        startRolling()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(owner: LifecycleOwner) {
        Log.d("Banner", "onPause")
        stopRolling()
    }

}