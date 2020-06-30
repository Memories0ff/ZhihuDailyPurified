package com.sion.zhihudailypurified.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.IndexBannerItemBinding
import com.sion.zhihudailypurified.databinding.IndexStoriesItemBinding
import com.sion.zhihudailypurified.entity.StoryBean
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator

class StoriesAdapter(private val fragment: StoriesFragment) :
    PagedListAdapter<StoryBean, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    var bannerAdapter: TopStoryBannerAdapter? = null
    var banner: Banner<*, *>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            BANNER -> {
                bannerAdapter = TopStoryBannerAdapter(fragment.vm.topStories.value!!)
                val bannerBinding: IndexBannerItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(fragment.activity),
                    R.layout.index_banner_item,
                    parent,
                    false
                )
                bannerBinding.banner.apply {
                    adapter = bannerAdapter!!
                    indicator = CircleIndicator(parent.context)
                    scrollTime = 100
                    currentItem = 0
                    banner = this
                    setOnBannerListener { _, position ->
                        Log.d(this.javaClass.name, "$position")
                        (fragment.activity as IndexActivity).switchToContent(
                            fragment,
                            ContentsDisplayFragment.TOP_STORIES,
                            position
                        )
                    }

                    bannerBinding.executePendingBindings()
                }
                return TopStoryViewHolder(bannerBinding)
            }
            else -> {
                val binding = DataBindingUtil.inflate<IndexStoriesItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.index_stories_item,
                    parent,
                    false
                )
                return StoryViewHolder(binding)
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
            is TopStoryViewHolder -> {
                holder.binding.banner.start()
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is TopStoryViewHolder) {
            holder.binding.banner.stop()
        }
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> BANNER
            else -> ITEM
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

    companion object {

        const val BANNER = 0
        const val ITEM = 1

        private val diffCallback = object : DiffUtil.ItemCallback<StoryBean>() {
            override fun areItemsTheSame(oldItem: StoryBean, newItem: StoryBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: StoryBean, newItem: StoryBean): Boolean =
                oldItem == newItem
        }
    }
}