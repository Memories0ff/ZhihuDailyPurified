package com.sion.zhihudailypurified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.CommentItemBinding
import com.sion.zhihudailypurified.entity.CommentBean
import com.sion.zhihudailypurified.view.fragment.CommentsFragment

class CommentsAdapter(val fragment: CommentsFragment) :
    PagedListAdapter<CommentBean, CommentsAdapter.CommentVH>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH = CommentVH(
        DataBindingUtil.inflate(
            LayoutInflater.from(fragment.context),
            R.layout.comment_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
        holder.binding.apply {
            comment = getItem(position)
        }
    }

    class CommentVH(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<CommentBean>() {
            override fun areItemsTheSame(oldItem: CommentBean, newItem: CommentBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CommentBean, newItem: CommentBean): Boolean =
                oldItem == newItem
        }
    }
}