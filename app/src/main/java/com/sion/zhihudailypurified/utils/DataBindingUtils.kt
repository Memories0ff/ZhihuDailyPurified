package com.sion.zhihudailypurified.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sion.zhihudailypurified.App

@BindingAdapter("bind:imageUrl")
fun loadImage(iv: ImageView, url: String) {
    Glide.with(App.getAppContext())
        .load(url)
        .into(iv)
}