package com.sion.zhihudailypurified.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.banner.Banner
import com.sion.zhihudailypurified.databinding.IndexFooterItemBinding
import com.sion.zhihudailypurified.databinding.IndexStoriesItemBinding
import com.sion.zhihudailypurified.datasource.PagedListLoadingStatus
import com.sion.zhihudailypurified.entity.StoryBean
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment

class StoriesAdapter(
    private val fragment: StoriesFragment,
    private var loadingStatus: PagedListLoadingStatus
) :
    PagedListAdapter<StoryBean, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    var banner: Banner? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM -> {
                val binding = DataBindingUtil.inflate<IndexStoriesItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.index_stories_item,
                    parent,
                    false
                )
                return StoryViewHolder(binding)
            }
            FOOTER -> {
                val binding = DataBindingUtil.inflate<IndexFooterItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.index_footer_item,
                    parent,
                    false
                )
                binding.llBtnRetry.setOnClickListener {
                    Toast.makeText(parent.context, "重新加载", Toast.LENGTH_SHORT).show()
                }
                return StoryFooterHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.index_top_banner_item, parent, false)
                banner = view.findViewById<Banner>(R.id.banner).apply {
                    setAdapter(
                        TopBannerAdapter(
                            fragment.vm.topStories.value!!,
                            fragment.activity!!
                        )
                    )
                }
                return TopStoryViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StoryViewHolder -> {
                val binding = holder.binding
                binding.story = getItem(position - 1)
                //获取是否已读
                binding.story!!.isRead.set(fragment.vm.isRead(position - 1, fragment.activity!!))
                binding.root.setOnClickListener {
                    (fragment.activity as IndexActivity).switchToContent(
                        fragment,
                        ContentsDisplayFragment.STORIES,
                        position - 1
                    )
                }
            }
            is StoryFooterHolder -> {
                val binding = holder.binding
                //这只能控制内部是否显示，不能控制本身是否显示
                binding.status = loadingStatus
            }
            //TopStoryViewHolder
            else -> {
                banner?.observeFragment(fragment)
            }
        }
    }

    override fun getItemCount(): Int =
        when (shouldShowFooter(loadingStatus)) {
            //需要显示footer
            true -> super.getItemCount() + 2    //内容+banner+footer
            //不需要显示footer
            false -> super.getItemCount() + 1    //内容+banner
        }


    //item的类别
    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> BANNER
            else -> {
                when (shouldShowFooter(loadingStatus)) {
                    true -> when (position) {
                        itemCount - 1 -> FOOTER
                        else -> ITEM
                    }
                    false -> ITEM
                }
            }
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


    /**
     * 滑动到指定位置
     */

    //目标项是否在最后一个可见项之后
    private var mShouldScroll: Boolean = false

    //记录目标项位置
    private var mToPosition: Int = 1

    fun smoothMoveToPosition(mRecyclerView: RecyclerView, position: Int) {
        // 第一个可见位置
        val firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        val lastItem = mRecyclerView.getChildLayoutPosition(
            mRecyclerView.getChildAt(
                mRecyclerView.childCount - 1
            )
        );
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            val movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.childCount) {
                val top = mRecyclerView.getChildAt(movePosition).top;
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false
                    smoothMoveToPosition(recyclerView, mToPosition)
                }
            }
        })
    }

    //继续加载列表
    fun continueLoad(position: Int) {
        //防止进入第一个滑到第二个再滑回第一个闪退
        if (position > 0) {
            getItem(position - 1)
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

    companion object {
        const val BANNER = 0
        const val ITEM = 1
        const val FOOTER = 2

        private val diffCallback = object : DiffUtil.ItemCallback<StoryBean>() {
            override fun areItemsTheSame(oldItem: StoryBean, newItem: StoryBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: StoryBean, newItem: StoryBean): Boolean =
                oldItem == newItem
        }
    }
}