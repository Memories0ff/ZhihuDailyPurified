package com.sion.zhihudailypurified.datasource

import androidx.paging.DataSource
import com.sion.zhihudailypurified.datasource.StoriesDataSource
import com.sion.zhihudailypurified.entity.StoryBean


class StoriesDataSourceFactory : DataSource.Factory<String, StoryBean>() {
    override fun create(): DataSource<String, StoryBean> =
        StoriesDataSource()
}