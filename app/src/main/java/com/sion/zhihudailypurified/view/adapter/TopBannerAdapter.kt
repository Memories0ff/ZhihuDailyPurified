package com.sion.zhihudailypurified.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sion.banner.BannerAdapter
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.databinding.BannerItemBinding
import com.sion.zhihudailypurified.entity.TopStoryBean
import com.sion.zhihudailypurified.view.activity.IndexActivity
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment

class TopBannerAdapter(
    private val dataSource: MutableList<TopStoryBean>,
    private val context: Context
) : BannerAdapter() {

    override fun getRealCount(): Int = dataSource.size

    override fun instantiateRealItem(container: ViewGroup, realPosition: Int): Any {
        val bean = dataSource[realPosition]
        val binding = DataBindingUtil.inflate<BannerItemBinding>(
            LayoutInflater.from(context),
            R.layout.banner_item,
            container,
            false
        )
        binding.topStory = bean
        container.addView(binding.root.apply {
            setOnClickListener {
                (context as IndexActivity).let {
                    it.switchToContent(
                        it.supportFragmentManager.findFragmentByTag(StoriesFragment.TAG) as StoriesFragment,
                        ContentsDisplayFragment.TOP_STORIES,
                        realPosition
                    )
                }
            }
        })
        return binding.root
    }

    override fun destroyRealItem(container: ViewGroup, realPosition: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

}