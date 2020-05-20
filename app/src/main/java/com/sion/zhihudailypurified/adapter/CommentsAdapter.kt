package com.sion.zhihudailypurified.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.CommentItemBinding
import com.sion.zhihudailypurified.utils.toast
import com.sion.zhihudailypurified.view.fragment.CommentsFragment
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentsAdapter(val fragment: CommentsFragment) :
    RecyclerView.Adapter<CommentsAdapter.CommentVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH = CommentVH(
        DataBindingUtil.inflate(
            LayoutInflater.from(fragment.context),
            R.layout.comment_item,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = fragment.vm.let { it.longCommentNum + it.shortCommentNum }

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
        holder.binding.apply {
            comment = fragment.vm.comments.value!![position]
            root.apply {
                tvBtnExpendOrCollapse.setOnClickListener {
                    holder.binding.comment!!.apply { isExpanded.set(!isExpanded.get()!!) }
                }
//                tvBtnExpendOrCollapse.viewTreeObserver.addOnPreDrawListener(object :
//                    ViewTreeObserver.OnPreDrawListener {
//                    override fun onPreDraw(): Boolean {
//                        holder.binding.comment!!.apply {
//                            quoteRealLineCount = tvQuote.lineCount
//                            isExpandable.set(reply_to != null && quoteRealLineCount > 2)
//                        }
//                        tvBtnExpendOrCollapse.viewTreeObserver.removeOnPreDrawListener(this)
//                        return false
//                    }
//                })
                post {
                    //????????????????????????????
                    holder.binding.comment!!.apply {
                        quoteRealLineCount = tvQuote.lineCount
                        Log.d("LineCount", "$quoteRealLineCount")
                        if (reply_to != null && quoteRealLineCount > 2) {
                            isExpandable.set(true)
                        } else {
                            isExpandable.set(false)
                        }
                    }
                }
            }
        }


    }

    class CommentVH(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)
}