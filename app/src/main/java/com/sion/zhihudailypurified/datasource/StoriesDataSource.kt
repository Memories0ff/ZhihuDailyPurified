package com.sion.zhihudailypurified.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sion.zhihudailypurified.db.dbStoryListServices
import com.sion.zhihudailypurified.entity.StoryBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO
import com.sion.zhihudailypurified.utils.obtainDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class StoriesDataSource : PageKeyedDataSource<String, StoryBean>() {

    private var loadedDays = 0

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, StoryBean>
    ) {
        postStatus(PagedListLoadingStatus.INITIAL_LOADING)
        apiServices.obtainLatest().callIO(
            onFailure = {
                it.printStackTrace()
                postStatus(PagedListLoadingStatus.INITIAL_FAILED)
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    callback.onResult(it.stories, null, it.date)
                    postStatus(PagedListLoadingStatus.INITIAL_LOADED)
                }
            },
            onResponseFailure = { response ->
                Log.e(
                    "StoriesDataSource",
                    "loadInitial: ${response.errorBody()?.string() ?: "Unknown error"}"
                )
                postStatus(PagedListLoadingStatus.INITIAL_FAILED)
            }
        )
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, StoryBean>) {
        postStatus(PagedListLoadingStatus.AFTER_LOADING)
        //删除之前保存的重试方法
        retry = null
        GlobalScope.launch(Dispatchers.Main) {
            val currentDate = obtainDay(Date(), -loadedDays - 1)
            val storyList =
                withContext(Dispatchers.IO) {
                    dbStoryListServices.obtainStoryBeansByDate(currentDate)
                        .sortedBy { it.loadingOrder }
                }
            //从数据库中获取到保存的数据
            if (!storyList.isNullOrEmpty()) {
                callback.onResult(storyList, currentDate)
                loadedDays++
                postStatus(PagedListLoadingStatus.AFTER_LOADED)
            }
            //未获取到则从网络中加载
            else {
                apiServices.obtainPastNews(obtainDay(Date(), -loadedDays)).callIO(
                    onFailure = {
                        //保存重试方法
                        it.printStackTrace()
                        retry = { loadAfter(params, callback) }
                        postStatus(PagedListLoadingStatus.AFTER_FAILED)
                    },
                    onResponseSuccess = { response ->
                        response.body()?.let { pastStories ->
                            var loadingOrder = 0
                            pastStories.stories.forEach { storyBean ->
                                storyBean.date = pastStories.date
                                storyBean.loadingOrder = loadingOrder
                                loadingOrder++
                            }
                            GlobalScope.launch(Dispatchers.IO) {
                                dbStoryListServices.insertStoryBeans(
                                    pastStories.stories
                                )
                            }
                            callback.onResult(pastStories.stories, pastStories.date)
                            loadedDays++
                            postStatus(PagedListLoadingStatus.AFTER_LOADED)
                        }
                    },
                    onResponseFailure = { response ->
                        Log.e(
                            "StoriesDataSource",
                            "loadAfter: ${response.errorBody()?.string() ?: "Unknown error"}"
                        )
                        //保存重试方法
                        retry = { loadAfter(params, callback) }
                        postStatus(PagedListLoadingStatus.AFTER_FAILED)
                    }
                )
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, StoryBean>) {

    }

    //对外提供只读的成员
    private val _loadingStatus = MutableLiveData<PagedListLoadingStatus>()
    val loadingStatus: LiveData<PagedListLoadingStatus>
        get() = _loadingStatus

    //向LiveData发送加载状态
    private fun postStatus(status: PagedListLoadingStatus) {
        _loadingStatus.postValue(status)
    }

    //保存当前方法用于重试
    var retry: (() -> Any?)? = null

}