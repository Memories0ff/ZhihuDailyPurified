package com.sion.zhihudailypurified.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sion.zhihudailypurified.view.fragment.ContentFragment
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment

/**
 * 此页面显示来自top和stories的新闻，
 * 需要区分加载的内容
 */

class ContentsVPAdapter(private val displayType: Int, private val fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment = ContentFragment(displayType).apply {
        arguments = Bundle().apply {
            putInt(
                ContentFragment.STORY_ID,
                when (displayType) {
                    //头条新闻
                    ContentsDisplayFragment.TOP_STORIES -> (fa.supportFragmentManager.findFragmentByTag(
                        StoriesFragment.TAG
                    ) as StoriesFragment).vm.topStories.value!![position].id
                    //普通新闻
                    else -> (fa.supportFragmentManager.findFragmentByTag(
                        StoriesFragment.TAG
                    ) as StoriesFragment).vm.stories.value!![position]!!.id
                }
            )
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return when (displayType) {
            ContentsDisplayFragment.TOP_STORIES -> {
                itemId in 0 until 5
            }
            else -> {
                itemId in 0 until ((fa.supportFragmentManager.findFragmentByTag(
                    StoriesFragment.TAG
                ) as StoriesFragment).vm.stories.value!!.size)
            }
        }
    }


    override fun getItemCount(): Int {
        return when (displayType) {
            //头条新闻
            ContentsDisplayFragment.TOP_STORIES -> (fa.supportFragmentManager.findFragmentByTag(
                StoriesFragment.TAG
            ) as StoriesFragment).vm.topStories.value!!.size
            //普通新闻
            else -> (fa.supportFragmentManager.findFragmentByTag(
                StoriesFragment.TAG
            ) as StoriesFragment).vm.stories.value!!.size
        }
    }

}