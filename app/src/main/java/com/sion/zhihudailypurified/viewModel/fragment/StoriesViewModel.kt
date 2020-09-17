package com.sion.zhihudailypurified.viewModel.fragment

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.datasource.StoriesDataSourceFactory
import com.sion.zhihudailypurified.entity.TopStoryBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO
import com.sion.zhihudailypurified.sharedPreference.spGetBoolean

class StoriesViewModel : BaseViewModel() {

    //头条新闻读取状态，只暴露不可变成员
    private val _topStoriesLoadingStatus = MutableLiveData<TopStoriesLoadingStatus>()
    val topStoriesLoadingStatus: LiveData<TopStoriesLoadingStatus>
        get() = _topStoriesLoadingStatus

    //新闻加载状态
    private val factory = StoriesDataSourceFactory()
    val pagedListLoadingStatus =
        Transformations.switchMap(factory.dataSource) { it.loadingStatus }

    //stories列表
    val stories = LivePagedListBuilder(
        factory,
        PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
    ).build()

    //头条
    private val _topStories = MutableLiveData<MutableList<TopStoryBean>>().apply {
        value = arrayListOf()
    }
    val topStories: LiveData<MutableList<TopStoryBean>>
        get() = _topStories

    //退出前文章的位置，给其赋值则列表滚动到对应位置
    val lastPos = MutableLiveData<Int>()

    //获取头条新闻
    fun obtainTopStories() {
        _topStoriesLoadingStatus.value = TopStoriesLoadingStatus.LOADING
        if (isOnline.value == false) {
            _topStoriesLoadingStatus.value = TopStoriesLoadingStatus.FAILED
            return
        }
        apiServices.obtainLatest().callIO(
            onFailure = {
                it.printStackTrace()
                _topStoriesLoadingStatus.postValue(TopStoriesLoadingStatus.FAILED)
            },
            onResponseFailure = { response ->
                _topStories.postValue(arrayListOf())
                _topStoriesLoadingStatus.postValue(TopStoriesLoadingStatus.FAILED)
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    _topStories.value!!.clear()
                    _topStories.value!!.addAll(it.top_stories)
                    _topStoriesLoadingStatus.value = TopStoriesLoadingStatus.LOADED
                    //通知数据加载完毕
                }
            }
        )
    }

    //列表加载失败的重试
    fun retry() {
        factory.dataSource.value?.retry?.invoke()
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
        factory.dataSource.value?.invalidate()
    }
}