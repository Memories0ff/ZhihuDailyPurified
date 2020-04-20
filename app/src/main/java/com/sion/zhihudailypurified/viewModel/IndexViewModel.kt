package com.sion.zhihudailypurified.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.datasource.StoriesDataSourceFactory
import com.sion.zhihudailypurified.entity.TopStoryBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO

class IndexViewModel : BaseViewModel() {

    val stories = LivePagedListBuilder(
        StoriesDataSourceFactory(),
        PagedList.Config.Builder()
            .setPageSize(15)
            .setEnablePlaceholders(false).build()
    ).build()

    val topStories = MutableLiveData<MutableList<TopStoryBean>>().apply {
        value = arrayListOf()
    }
    val updateTopStories = MutableLiveData<Boolean>()

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
}