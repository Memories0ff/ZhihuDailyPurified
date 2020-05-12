package com.sion.zhihudailypurified.viewModel.fragment

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.datasource.StoriesDataSourceFactory
import com.sion.zhihudailypurified.entity.TopStoryBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO
import com.sion.zhihudailypurified.sharedPreference.spGetBoolean
import com.sion.zhihudailypurified.sharedPreference.spPutBoolean
import com.sion.zhihudailypurified.view.fragment.StoriesFragment

class StoriesViewModel : BaseViewModel() {
    //新闻列表

    val stories = LivePagedListBuilder(
        StoriesDataSourceFactory(),
        PagedList.Config.Builder()
            .setPageSize(15)
            .setEnablePlaceholders(false)
            .build()
    ).build()

    val topStories = MutableLiveData<MutableList<TopStoryBean>>().apply {
        value = arrayListOf()
    }
    val updateTopStories = MutableLiveData<Boolean>()

    //退出前文章的位置，给其赋值则列表滚动到对应位置
    val lastPos = MutableLiveData<Int>()

    //获取头条新闻
    fun obtainTopStories() {
        apiServices.obtainLatest().callIO(
            onFailure = {
                it.printStackTrace()
            },
            onResponseFailure = { response ->
                topStories.value = arrayListOf()
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    topStories.value!!.clear()
                    topStories.value!!.addAll(it.top_stories)
                    updateTopStories.value = true
                }
            }
        )
    }

    //获取是否已读
    fun isRead(position: Int, activity: FragmentActivity): Boolean {
        stories.value!![position]!!.apply {
            return spGetBoolean(id.toString(), activity)
        }
    }
}