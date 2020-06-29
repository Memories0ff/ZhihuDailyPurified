package com.sion.zhihudailypurified.view.itemDecoration

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.view.fragment.CommentsFragment

class CommentsDecoration(private val commentsFragment: CommentsFragment) :
    RecyclerView.ItemDecoration() {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF000000")
        textSize =
            commentsFragment.context!!.resources.getDimensionPixelSize(R.dimen.comments_item_decoration_text_size)
                .toFloat()
        typeface = Typeface.DEFAULT_BOLD
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1F000000")
        strokeWidth = decorationUnderlineHeight.toFloat()
    }

    private val decorationHeight =
        commentsFragment.context!!.resources.getDimensionPixelSize(R.dimen.comments_item_decoration_height)

    private val decorationMarginLeft =
        commentsFragment.context!!.resources.getDimensionPixelSize(R.dimen.comments_item_decoration_margin_left)

    private val decorationUnderlineHeight =
        commentsFragment.context!!.resources.getDimensionPixelSize(R.dimen.comments_item_underline_height)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        //排除越界错误
        if (position >= commentsFragment.vm.commentsNum) {
            return
        }
        //留出显示长评短评个数的空间

        if (position == 0 || position == commentsFragment.vm.longCommentsNum) {
            outRect.set(0, decorationHeight, 0, decorationUnderlineHeight)  //留出下划线空间
        }

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager
        val basePosition = linearLayoutManager.findFirstVisibleItemPosition()
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val exactPosition = basePosition + i
            if (exactPosition < 0 || exactPosition >= commentsFragment.vm.commentsNum) {
                return
            }
            //绘制长评短评个数
            if (commentsFragment.vm.longCommentsNum == 0 && exactPosition == 0) {
                //短评
                drawText(
                    c,
                    parent.getChildAt(i),
                    parent,
                    "${commentsFragment.vm.shortCommentsNum}条短评"
                )
            } else if (exactPosition == 0) {
                //长评
                drawText(
                    c,
                    parent.getChildAt(i),
                    parent,
                    "${commentsFragment.vm.longCommentsNum}条长评"
                )
            } else if (exactPosition == commentsFragment.vm.longCommentsNum) {
                //短评
                drawText(
                    c,
                    parent.getChildAt(i),
                    parent,
                    "${commentsFragment.vm.shortCommentsNum}条短评"
                )
            }
            //绘制下划线
            drawUnderline(c, parent.getChildAt(i), parent)
        }
    }

    private fun drawText(c: Canvas, childView: View, parent: RecyclerView, dateStr: String) {
        val top =
            (childView.top - decorationHeight).toFloat()
        val left = decorationMarginLeft.toFloat()
        val bottom = childView.top.toFloat()
        val height = bottom - top
        val midY = top + height / 2
        c.drawText(
            dateStr,
            left,
            midY + (textPaint.textSize / 2.25/*上下居中文字*/).toInt(),
            textPaint
        )
    }

    private fun drawUnderline(c: Canvas, childView: View, parent: RecyclerView) {
        val top = childView.bottom.toFloat()
        val left = 0f
        val right = childView.right.toFloat()
        (childView.bottom + decorationUnderlineHeight).toFloat()
        c.drawLine(left, top, right, top, linePaint)
    }
}