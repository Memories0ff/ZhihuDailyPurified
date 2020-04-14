package com.sion.zhihudailypurified.datasource

import androidx.paging.PageKeyedDataSource
import com.sion.zhihudailypurified.entity.StoryBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO
import com.sion.zhihudailypurified.utils.obtainDay
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
        apiServices.obtainPastNews(obtainDay(Date(), -loadedDays)).callIO(
            onFailure = {
                it.printStackTrace()
            },
            onResponseSuccess = { response ->
                response.body()?.let { pastStories ->
                    pastStories.stories.forEach { storyBean ->
                        storyBean.date = pastStories.date
                    }
                    callback.onResult(pastStories.stories, pastStories.date)
                    loadedDays++
                }
            }
        )
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, StoryBean>) {

    }
}