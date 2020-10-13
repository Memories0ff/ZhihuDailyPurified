package com.sion.zhihudailypurified.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sion.zhihudailypurified.entity.CommentBean

class CommentsDataSourceFactory(private val storyId: Int) : DataSource.Factory<Int, CommentBean>() {

    val dataSource = MutableLiveData<CommentsDataSource>()

    override fun create(): DataSource<Int, CommentBean> =
        CommentsDataSource(storyId).also {
            dataSource.postValue(it)
        }
}