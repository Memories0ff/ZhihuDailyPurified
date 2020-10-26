package com.sion.zhihudailypurified.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.CommentItemBinding
import com.sion.zhihudailypurified.databinding.CommentsFooterItemBinding
import com.sion.zhihudailypurified.datasource.PagedListLoadingStatus
import com.sion.zhihudailypurified.entity.CommentBean
import com.sion.zhihudailypurified.view.fragment.CommentsFragment
import com.sion.zhihudailypurified.viewModel.fragment.CommentsViewModel

class CommentsAdapter(
    val fragment: CommentsFragment,
    var loadingStatus: PagedListLoadingStatus,
    val vm: CommentsViewModel
) :
    PagedListAdapter<CommentBean, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM -> CommentVH(
                DataBindingUtil.inflate<CommentItemBinding>(
                    LayoutInflater.from(fragment.context),
                    R.layout.comment_item,
                    parent,
                    false
                ).apply {
                    commentLikeImageField = ObservableField<Boolean>()
                    ivBtnCommentLike.setOnClickListener {
                        if (fragment.isOnline() == true) {
                            if (vm.queryCommentLike(comment!!.id, comment!!.time)) {
                                //已点过赞，取消点赞
                                vm.removeCommentLike(comment!!.id, comment!!.time)
                                commentLikeImageField!!.set(false)
                            } else {
                                //未点过赞，继续操作
                                vm.insertCommentLike(comment!!.id, comment!!.time)
                                commentLikeImageField!!.set(true)
                            }
                        } else {
                            fragment.apply {
                                toast(
                                    resources.getText(R.string.retry_after_resume_connection)
                                        .toString()
                                )
                            }
                        }
                    }
                }
            )
            else -> CommentsFooterHolder(DataBindingUtil.inflate<CommentsFooterItemBinding>(
                LayoutInflater.from(fragment.context),
                R.layout.comments_footer_item,
                parent,
                false
            ).apply {
                llBtnRetry.setOnClickListener {
                    vm.retry()
                }
            })
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentVH -> holder.binding.apply {
                comment = getItem(position)
                commentLikeImageField!!.set(vm.queryCommentLike(comment!!.id, comment!!.time))
            }
            else -> {
                (holder as CommentsFooterHolder).binding.status = loadingStatus
            }
        }
    }

    override fun getItemCount(): Int =
        when (shouldShowFooter(loadingStatus)) {
            true -> super.getItemCount() + 1
            false -> super.getItemCount()
        }

    override fun getItemViewType(position: Int): Int =
        when (shouldShowFooter(loadingStatus)) {
            true -> {
                when (position) {
                    itemCount - 1 -> FOOTER
                    else -> ITEM
                }
            }
            false -> ITEM
        }

    //是否需要显示footer
    private fun shouldShowFooter(loadingStatus: PagedListLoadingStatus): Boolean =
        when (loadingStatus) {
            PagedListLoadingStatus.INITIAL_LOADED,
            PagedListLoadingStatus.AFTER_LOADED -> {
                false
            }
            else -> {
                true
            }
        }

    fun updateLoadingStatus(status: PagedListLoadingStatus) {
        //暂存之前的状态
        val pre = loadingStatus
        //修改loadingStatus
        loadingStatus = status
        //修改界面
        if (shouldShowFooter(pre)) {
            //显示->显示
            if (shouldShowFooter(loadingStatus)) {
                notifyItemChanged(itemCount - 1)
            }
            //显示->不显示
            else {
                notifyItemRemoved(itemCount - 1)
            }
        } else {
            //不显示->显示
            if (shouldShowFooter(loadingStatus)) {
                notifyItemInserted(itemCount)
            }
        }
    }

    class CommentVH(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ITEM = 0
        const val FOOTER = 1

        private val diffCallback = object : DiffUtil.ItemCallback<CommentBean>() {
            override fun areItemsTheSame(oldItem: CommentBean, newItem: CommentBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CommentBean, newItem: CommentBean): Boolean =
                oldItem == newItem
        }
    }
}