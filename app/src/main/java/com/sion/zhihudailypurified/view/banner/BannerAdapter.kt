package com.sion.zhihudailypurified.view.banner

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.BannerItemBinding
import com.sion.zhihudailypurified.entity.TopStoryBean

class BannerAdapter(
    val dataSource: MutableList<TopStoryBean>,
    val activity: Activity
) :
    RecyclerView.Adapter<BannerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<BannerItemBinding>(
            LayoutInflater.from(activity),
            R.layout.banner_item,
            parent,
            false
        )
        return BannerViewHolder(
            viewDataBinding
        )
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bannerItemBinding.topStory = dataSource[position]
        holder.bannerItemBinding.root.setOnClickListener {
            callback?.invoke(position)
            Log.d("BannerAdapter", "点击项：$position")
        }
        holder.bannerItemBinding.executePendingBindings()
    }

    private var callback: ((position: Int) -> Unit)? = null
    fun setCallback(f: (position: Int) -> Unit) {
        callback = f
    }
}