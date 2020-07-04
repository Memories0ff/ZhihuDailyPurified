package com.sion.zhihudailypurified.components.banner

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.widget.ViewPager2
import com.rd.PageIndicatorView
import com.rd.PageIndicatorView2
import com.sion.zhihudailypurified.R
import java.util.*

class Banner(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    LifecycleObserver {

    private var activity: Activity? = null
    private var period: Long = 5000  //ms

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.banner, this, false)
        addView(view)
    }

    //true:显示内容，消除占位符;false:只显示占位符
//    fun isLoadFinish(value: Boolean) {
//        viewPager2.visibility = if (value) View.VISIBLE else View.GONE
//        indicator.visibility = if (value) View.VISIBLE else View.GONE
//        placeholder.visibility = if (value) View.GONE else View.VISIBLE
//    }

    fun observeFragment(fragment: Fragment) {
        if (this.activity == null) {
            this.activity = fragment.activity
            fragment.lifecycle.addObserver(this)
        }
    }

    private val viewPager2 by lazy {
        findViewById<ViewPager2>(R.id.vp2)
    }

    private val indicator by lazy {
        findViewById<PageIndicatorView2>(R.id.indicator)
    }

    private val placeholder by lazy {
        findViewById<ProgressBar>(R.id.placeholder)
    }

    private var adapter: BannerAdapter? = null

    fun setAdapter(adapter: BannerAdapter) {
        this.adapter = adapter
        viewPager2.adapter = adapter
        indicator.count = adapter.dataSource.size
        adapter.notifyDataSetChanged()
    }

    fun getAdapter(): BannerAdapter? {
        return adapter
    }

    fun setCurrentPosition(position: Int) {
        adapter?.let {
            if (it.dataSource.size != 0) {
                viewPager2.currentItem = position
                indicator.selection = position % it.dataSource.size
            }
        }
    }

    fun setOnItemClickListener(callback: (position: Int) -> Unit) {
        adapter?.setCallback(callback)
    }

    fun getCurrentPosition(): Int = viewPager2.currentItem

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private fun startJump() {
        if (timer == null) {
            timer = Timer()
        }
        if (timerTask == null) {
            timerTask = object : TimerTask() {
                override fun run() {
                    adapter?.let {
                        activity?.runOnUiThread {
                            setCurrentPosition((viewPager2.currentItem + 1) % it.dataSource.size)
                        }
                    }
                }
            }
        }
        timer!!.schedule(timerTask, period, period)
    }

    private fun stopJump() {
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
        startJump()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(owner: LifecycleOwner) {
        Log.d("Banner", "onPause")
        stopJump()
    }

}