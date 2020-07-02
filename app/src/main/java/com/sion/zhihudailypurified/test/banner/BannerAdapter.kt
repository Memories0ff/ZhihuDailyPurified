package com.sion.zhihudailypurified.test.banner

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.BannerItemBinding
import com.sion.zhihudailypurified.entity.TopStoryBean

class BannerAdapter(
    val dataSource: MutableList<TopStoryBean>,
    val context: Context
) :
    RecyclerView.Adapter<BannerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<BannerItemBinding>(
            LayoutInflater.from(context),
            R.layout.banner_item,
            parent,
            false
        )
        return BannerViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bannerItemBinding.topStory = dataSource[position]
        holder.bannerItemBinding.executePendingBindings()
    }
}