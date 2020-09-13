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

class StoriesViewModel : BaseViewModel() {

    val loadingError = MutableLiveData(false)

    //新闻列表
    val stories = LivePagedListBuilder(
        StoriesDataSourceFactory(),
        PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
    ).build()

    val topStories = MutableLiveData<MutableList<TopStoryBean>>().apply {
        value = arrayListOf()
    }

    //赋值true则通知头条新闻加载完成
    val loadTopFinished: MutableLiveData<Boolean> = MutableLiveData(false)

    //退出前文章的位置，给其赋值则列表滚动到对应位置
    val lastPos = MutableLiveData<Int>()

    //获取头条新闻
    fun obtainTopStories() {
        if (isOnline.value == false) {
            loadingError.value = true
            return
        }
        apiServices.obtainLatest().callIO(
            onFailure = {
                it.printStackTrace()
                loadingError.postValue(true)
            },
            onResponseFailure = { response ->
                topStories.postValue(arrayListOf())
                loadingError.value = true
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    topStories.value!!.clear()
                    topStories.value!!.addAll(it.top_stories)
                    loadTopFinished.value = true    //通知数据加载完毕
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

    //数据更新
    fun updateData() {
        obtainTopStories()
    }
}