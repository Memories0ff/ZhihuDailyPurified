package com.sion.zhihudailypurified.adapter

import android.app.Activity
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
import com.sion.zhihudailypurified.sharedPreference.spGetBoolean
import com.sion.zhihudailypurified.sharedPreference.spPutBoolean
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
                val bannerBinding: IndexBannerItemBinding
                fragment.let {
                    bannerAdapter = TopStoryBannerAdapter(it.vm.topStories.value!!)
                    bannerBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(it.activity),
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
//                binding.story!!.isRead =
//                    spGetBoolean(binding.story!!.id.toString(), fragment.activity as Activity)
                binding.root.setOnClickListener {
                    (fragment.activity as IndexActivity).switchToContent(
                        fragment,
                        ContentsDisplayFragment.STORIES,
                        position - 1
                    )
                    //???????????????????????????????应该在进入fragment并显示后执行此操作，否则左右滑动不会标记或标记上预加载的未读的新闻
//                    if (!binding.story!!.isRead) {
//                        binding.story!!.isRead = true
//                        spPutBoolean(
//                            binding.story!!.id.toString(),
//                            true,
//                            fragment.activity as Activity
//                        )
//                    }
                    //???????????????????????????????
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