package com.sion.zhihudailypurified.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.GalleryItemBinding
import com.sion.zhihudailypurified.view.fragment.ImagesGalleryFragment
import com.sion.zhihudailypurified.viewModel.fragment.ImagesGalleryViewModel

class GalleryVPAdapter(val fragment: ImagesGalleryFragment, val vm: ImagesGalleryViewModel) :
    RecyclerView.Adapter<GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        DataBindingUtil.inflate<GalleryItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.gallery_item,
            parent,
            false
        ).apply {
            return GalleryViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        Glide.with(fragment)
            .load(vm.picUrls[position])
            .placeholder(R.drawable.ic_baseline_pic_placeholder_96)
            .error(R.drawable.ic_baseline_pic_broken_96)
            .into(holder.binding.pvImage)
    }

    override fun getItemCount(): Int = vm.picUrls.size
}