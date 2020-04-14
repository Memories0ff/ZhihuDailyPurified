package com.sion.zhihudailypurified.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.TopStoryBannerItemBinding
import com.sion.zhihudailypurified.entity.TopStoryBean
import com.youth.banner.adapter.BannerAdapter

class TopStoryBannerAdapter(datas: MutableList<TopStoryBean>) :
    BannerAdapter<TopStoryBean, TopStoryBannerAdapter.BannerViewHolder>(datas) {


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {

        val binding = DataBindingUtil.inflate<TopStoryBannerItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.top_story_banner_item,
            parent,
            false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: TopStoryBean,
        position: Int,
        size: Int
    ) {
        val binding = holder.binding
        binding.topStory = data
        binding.executePendingBindings()
    }


    inner class BannerViewHolder(val binding: TopStoryBannerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}