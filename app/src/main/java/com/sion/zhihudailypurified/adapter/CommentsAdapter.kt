package com.sion.zhihudailypurified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.CommentItemBinding
import com.sion.zhihudailypurified.view.fragment.CommentsFragment

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

    override fun getItemCount(): Int =
        fragment.vm.let { it.currentLongCommentNum + it.currentShortCommentNum }

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
        holder.binding.apply {
            comment = fragment.vm.comments.value!![position]
        }
    }

    class CommentVH(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)
}