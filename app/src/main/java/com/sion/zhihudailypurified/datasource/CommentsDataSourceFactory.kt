package com.sion.zhihudailypurified.datasource

import androidx.paging.DataSource
import com.sion.zhihudailypurified.entity.CommentBean

class CommentsDataSourceFactory(private val storyId: Int) : DataSource.Factory<Int, CommentBean>() {
    override fun create(): DataSource<Int, CommentBean> =
        CommentsDataSource(storyId)
}