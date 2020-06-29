package com.sion.zhihudailypurified.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.sion.zhihudailypurified.entity.CommentBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentsDataSource(private val id: Int) : PageKeyedDataSource<Int, CommentBean>() {

    var commentId = 0
    var pageNum = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CommentBean>
    ) {
        Log.d("CommentsDataSource", "requestedLoadSize:${params.requestedLoadSize}")
        GlobalScope.launch(Dispatchers.Main) {
            val result = arrayListOf<CommentBean>()
            withContext(Dispatchers.IO) {
                apiServices.obtainLongComments(id.toString()).execute().body()
                    ?.let { commentList ->
                        result.addAll(commentList.comments)
                    }
            }
            val longCommentsNum = result.size
            withContext(Dispatchers.IO) {
                apiServices.obtainShortComments(id.toString()).execute().body()
                    ?.let { commentList ->
                        result.addAll(commentList.comments)
                    }
            }
            //设置第一条长评短评标记
            result.first().isFirstLongComment = true
            result[longCommentsNum].isFirstShortComment = true
            commentId = result.last().id
            callback.onResult(result, null, pageNum)
            pageNum++
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CommentBean>
    ) {
        Log.d(
            "CommentsDataSource",
            "key:${params.key} | requestedLoadSize:${params.requestedLoadSize}"
        )
        apiServices.obtainShortComments(id.toString(), commentId.toString()).callIO(
            onFailure = {
                it.printStackTrace()
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    if (!it.comments.isNullOrEmpty()) {
                        commentId = it.comments.last().id
                        callback.onResult(it.comments, pageNum)
                        pageNum++
                    }
                }
            }
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CommentBean>
    ) {

    }
}