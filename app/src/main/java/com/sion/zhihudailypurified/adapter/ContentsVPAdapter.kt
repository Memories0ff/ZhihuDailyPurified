package com.sion.zhihudailypurified.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sion.zhihudailypurified.view.fragment.ContentFragment
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment

class ContentsVPAdapter(private val displayType: Int, private val fa: FragmentActivity) :
    FragmentStatePagerAdapter(
        fa.supportFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(position: Int): Fragment = ContentFragment(displayType).apply {
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

    override fun getCount(): Int {
        return when (displayType) {
            //头条新闻
            ContentsDisplayFragment.TOP_STORIES -> (fa.supportFragmentManager.findFragmentByTag(
                StoriesFragment.TAG
            ) as StoriesFragment).vm.topStories.value!!.size
            //普通新闻
            else -> Int.MAX_VALUE
        }
    }

}