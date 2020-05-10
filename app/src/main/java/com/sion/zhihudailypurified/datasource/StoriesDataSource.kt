package com.sion.zhihudailypurified.datasource

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
        apiServices.obtainLatest().callIO(
            onFailure = {
                it.printStackTrace()
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    callback.onResult(it.stories, null, it.date)
                }
            }
        )
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, StoryBean>) {
        GlobalScope.launch(Dispatchers.Main) {
            val currentDate = obtainDay(Date(), -loadedDays - 1)
            val storyList =
                withContext(Dispatchers.IO) {
                    dbStoryListServices.obtainStoryBeansByDate(currentDate)
                        .sortedBy { it.loadingOrder }
                }
            if (!storyList.isNullOrEmpty()) {
                callback.onResult(storyList, currentDate)
                loadedDays++
            } else {
                apiServices.obtainPastNews(obtainDay(Date(), -loadedDays)).callIO(
                    onFailure = {
                        it.printStackTrace()
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
                        }
                    }
                )
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, StoryBean>) {

    }
}