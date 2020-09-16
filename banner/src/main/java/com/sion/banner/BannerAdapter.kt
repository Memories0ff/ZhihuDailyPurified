package com.sion.banner

import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

abstract class BannerAdapter : PagerAdapter() {

    abstract fun getRealCount(): Int

    abstract fun instantiateRealItem(container: ViewGroup, realPosition: Int): Any

    abstract fun destroyRealItem(container: ViewGroup, realPosition: Int, `object`: Any)

    fun getRealPosition(position: Int): Int = position % getRealCount()

    fun getInitialPosition(): Int = PAGE_NUM / 2 - (PAGE_NUM / 2) % getRealCount()//TODO 此处存在除0错误(是否解决未知)

    final override fun instantiateItem(container: ViewGroup, position: Int): Any =
        instantiateRealItem(container, getRealPosition(position))

    final override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        destroyRealItem(container, getRealPosition(position), `object`)
    }

    final override fun getCount(): Int =
        PAGE_NUM

    companion object {
        private const val PAGE_NUM = Int.MAX_VALUE
    }
}