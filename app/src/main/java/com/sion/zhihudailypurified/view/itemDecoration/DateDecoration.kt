package com.sion.zhihudailypurified.view.itemDecoration

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.datasource.PagedListLoadingStatus
import com.sion.zhihudailypurified.utils.dp
import com.sion.zhihudailypurified.utils.eightDateDeleteZeroAndYear
import com.sion.zhihudailypurified.view.adapter.StoriesAdapter
import com.sion.zhihudailypurified.view.fragment.StoriesFragment

class DateDecoration(private val storiesFragment: StoriesFragment) : RecyclerView.ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#7F000000")
        textSize =
            storiesFragment.context!!.resources.getDimensionPixelSize(R.dimen.index_stories_item_decoration_text_size)
                .toFloat()
        typeface = Typeface.DEFAULT_BOLD
    }
    private val decorationHeight = 20.dp
    //        storiesFragment.context!!.resources.getDimensionPixelSize(R.dimen.index_stories_item_decoration_height)

    private val decorationMarginLeft = 15.dp
    //        storiesFragment.context!!.resources.getDimensionPixelSize(R.dimen.index_stories_item_decoration_margin_left)

    private val decorationMarginRight = 15.dp
//        storiesFragment.context!!.resources.getDimensionPixelSize(R.dimen.index_stories_item_decoration_margin_right)


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) - 1 //去掉头部轮播图
        val storiesData = storiesFragment.vm.stories.value ?: return
        // 忽略轮播图，排除越界错误
        if (position < 0 || position >= storiesData.size) {
            return
        }
        val storyData = storiesData[position] ?: return
        if (storyData.date != null && storyData.loadingOrder == 0) {
            //留出绘制空间
            outRect.set(0, decorationHeight, 0, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager
        val childCount = parent.childCount
        val basePosition = linearLayoutManager.findFirstVisibleItemPosition() - 1 //去掉头部轮播图
        val storiesData = storiesFragment.vm.stories.value ?: return
        for (i in 0 until childCount) {
            val exactPosition = basePosition + i
            //忽略轮播图且当天第一项不添加Decoration，排除越界错误
            if (exactPosition < 1 || exactPosition >= storiesData.size) {
                continue
            }
            val storyData = storiesData[exactPosition] ?: continue
            if (storyData.date != null && storyData.loadingOrder == 0) {
                //绘制头部日期
                drawDate(
                    c,
                    parent.getChildAt(i),
                    parent,
                    eightDateDeleteZeroAndYear(storyData.date)
                )
            }
        }
    }

    //绘制头部日期
    private fun drawDate(c: Canvas, childView: View, parent: RecyclerView, dateStr: String) {
        val top =
            (childView.top - decorationHeight).toFloat()
        val left = decorationMarginLeft.toFloat()
        val right =
            (childView.right - decorationMarginRight).toFloat()
        val bottom = childView.top.toFloat()
        val height = bottom - top
        val midY = top + height / 2
        c.drawText(dateStr, left, midY + (paint.textSize / 2.25/*文字中线与横线对齐*/).toInt(), paint)
        val lineStartX = left + paint.measureText(dateStr) + decorationMarginLeft
        c.drawLine(lineStartX, midY, right, midY, paint)
    }

    private fun hasFooter(adapter: StoriesAdapter): Boolean {
        return adapter.loadingStatus in arrayListOf(
            PagedListLoadingStatus.INITIAL_LOADING,
            PagedListLoadingStatus.INITIAL_FAILED,
            PagedListLoadingStatus.AFTER_LOADING,
            PagedListLoadingStatus.AFTER_FAILED,
            PagedListLoadingStatus.COMPLETED
        )
    }

    companion object {
        const val TAG = "DateDecoration"
    }
}