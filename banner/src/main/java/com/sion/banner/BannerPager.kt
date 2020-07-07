package com.sion.banner

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class BannerPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    init {
        offscreenPageLimit = 1
    }

    var adapter: BannerAdapter? = null
        set(value) {
            if (value != null) {
                field = value
                super.setAdapter(value)
                super.setCurrentItem(value.getInitialPosition())
            } else {
                throw Exception("不可传入null值")
            }
        }

    override fun getAdapter(): PagerAdapter {
        if (adapter == null) {
            throw Exception("adapter不能为空")
        }
        return adapter!!
    }

    /**
     * 跳转到轮播图的某个位置
     * @param realPosition 轮播图一循环中的真正位置
     */
    override fun setCurrentItem(realPosition: Int) {
        if (realPosition < 0 || realPosition >= adapter!!.getRealCount()) {
            throw Exception("设置的位置不正确")
        }
        val currentItem = adapter!!.getRealPosition(super.getCurrentItem())
        super.setCurrentItem(
            //当前页在跳转页左边
            if (currentItem < realPosition) {
                //距离不远，向左滚动到跳转页
                if (abs(currentItem - realPosition) <= adapter!!.getRealCount() / 2) {
                    super.getCurrentItem() - super.getCurrentItem() % adapter!!.getRealCount() + realPosition
                }
                //距离远，向右滚动到跳转页
                else {
                    super.getCurrentItem() - super.getCurrentItem() % adapter!!.getRealCount() - adapter!!.getRealCount() + realPosition
                }
            }
            //当前页在跳转页右边
            else if (currentItem > realPosition) {
                //距离不远，向右滚动到跳转页
                if (abs(currentItem - realPosition) <= adapter!!.getRealCount() / 2) {
                    super.getCurrentItem() - super.getCurrentItem() % adapter!!.getRealCount() + realPosition
                }
                //距离远，向左滚动到跳转页
                else {
                    super.getCurrentItem() - super.getCurrentItem() % adapter!!.getRealCount() + adapter!!.getRealCount() + realPosition
                }
            }
            //不动
            else {
                super.getCurrentItem()
            }
        )
    }


}