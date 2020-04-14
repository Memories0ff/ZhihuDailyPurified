package com.sion.zhihudailypurified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.IndexBannerItemBinding
import com.sion.zhihudailypurified.databinding.IndexStoriesItemBinding
import com.sion.zhihudailypurified.entity.StoryBean
import com.sion.zhihudailypurified.entity.TopStoryBean
import com.sion.zhihudailypurified.view.IndexActivity
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator

class StoriesAdapter :
    PagedListAdapter<StoryBean, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    private val BANNER = 0
    private val LIST_ITEM = 1

    private lateinit var topBanner: Banner<Any, BannerAdapter<*, *>>
    private lateinit var topBannerHolder: TopStoryViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            BANNER -> {
                val binding = DataBindingUtil.inflate<IndexBannerItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.index_banner_item,
                    parent,
                    false
                )
                return TopStoryViewHolder(binding)
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
            is TopStoryViewHolder -> {
                topBannerHolder = holder
                val binding = holder.binding
                topBanner = binding.banner
                //////////////////////////////////////
                //设置轮播图
                (holder.binding.root.context as IndexActivity).let { activity ->
                    activity.vm.let { ivm ->
                        if (ivm.topStories.value.isNullOrEmpty()) {
                            ivm.topStories.value = arrayListOf()
                        }
                        topBanner.let { banner ->
                            banner.adapter = TopStoryBannerAdapter(ivm.topStories.value!!)
                            banner.indicator = CircleIndicator(activity)
                            ivm.topStories.observe(activity, Observer {
                                updateBanner()
                            })
                            ivm.bannerRunning.observe(activity, Observer {
                                if (it) {
                                    startBanner()
                                } else {
                                    stopBanner()
                                }
                            })
                        }
                    }
                }
                //////////////////////////////////////
                binding.executePendingBindings()
                startBanner()
            }
            else -> {
                holder as StoryViewHolder
                val binding = holder.binding
                binding.story = getItem(position - 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> BANNER
            else -> LIST_ITEM
        }
    }

    fun startBanner() {
        topBannerHolder.binding.banner.start()
    }

    fun stopBanner() {
        topBannerHolder.binding.banner.stop()
    }

    fun updateBanner() {
        topBanner.adapter.notifyDataSetChanged()
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StoryBean>() {
            override fun areItemsTheSame(oldItem: StoryBean, newItem: StoryBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: StoryBean, newItem: StoryBean): Boolean =
                oldItem == newItem
        }
    }
}