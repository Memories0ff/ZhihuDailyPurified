package com.sion.zhihudailypurified.adapter

import android.app.Activity
import android.content.Intent
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
import com.sion.zhihudailypurified.view.ContentActivity
import com.sion.zhihudailypurified.view.IndexActivity
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator

class StoriesAdapter :
    PagedListAdapter<StoryBean, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    var bannerAdapter: TopStoryBannerAdapter? = null
    var banner: Banner<*, *>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            BANNER -> {
                val bannerBinding: IndexBannerItemBinding
                (parent.context as IndexActivity).let {
                    bannerAdapter = TopStoryBannerAdapter(it.vm.topStories.value!!)
                    bannerBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(it),
                        R.layout.index_banner_item,
                        parent,
                        false
                    )
                    bannerBinding.banner.adapter = bannerAdapter!!
                    bannerBinding.banner.indicator = CircleIndicator(parent.context)
                    banner = bannerBinding.banner
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
                binding.root.setOnClickListener {
                    (binding.root.context as IndexActivity).startActivity(
                        Intent(
                            it.context,
                            ContentActivity::class.java
                        ).apply {
                            putExtra(ContentActivity.STORY_ID, binding.story!!.id)
                        })
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