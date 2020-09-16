package com.sion.zhihudailypurified.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sion.zhihudailypurified.entity.StoryBean


class StoriesDataSourceFactory : DataSource.Factory<String, StoryBean>() {

    val dataSource = MutableLiveData<StoriesDataSource>()

    override fun create(): DataSource<String, StoryBean> =
        StoriesDataSource().also {
            dataSource.postValue(it)
        }
}