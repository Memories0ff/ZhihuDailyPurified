package com.sion.zhihudailypurified.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    var isInitialFailed = false

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CommentBean>
    ) {
        isInitialFailed = false
        postLoadingStatus(PagedListLoadingStatus.INITIAL_LOADING)
        Log.d("CommentsDataSource", "requestedLoadSize:${params.requestedLoadSize}")
        GlobalScope.launch(Dispatchers.Main) {
            val result = arrayListOf<CommentBean>()
            withContext(Dispatchers.IO) {
                //TODO 未处理异常:SocketTimeoutException: timeout(待测试)
                try {
                    apiServices.obtainLongComments(id.toString()).execute().body()
                        ?.let { commentList ->
                            result.addAll(commentList)
                        }
                } catch (e: Exception) {
                    postLoadingStatus(PagedListLoadingStatus.INITIAL_FAILED)
                    isInitialFailed = true
                }
            }
            if (isInitialFailed) {
                return@launch
            }
            val longCommentsNum = result.size
            withContext(Dispatchers.IO) {
                //TODO 未处理异常:SocketTimeoutException: timeout(待测试)
                try {
                    apiServices.obtainShortComments(id.toString()).execute().body()
                        ?.let { commentList ->
                            result.addAll(commentList)
                        }
                } catch (e: Exception) {
                    postLoadingStatus(PagedListLoadingStatus.INITIAL_FAILED)
                    isInitialFailed = true
                }
            }
            if (isInitialFailed) {
                return@launch
            }
            if (!result.isNullOrEmpty()) {
                //设置第一条长评短评标记
                result.first().isFirstLongComment = true
                result[longCommentsNum].isFirstShortComment = true
                commentId = result.last().id
                callback.onResult(result, null, pageNum)
                pageNum++
                postLoadingStatus(PagedListLoadingStatus.INITIAL_LOADED)
            } else {
                postLoadingStatus(PagedListLoadingStatus.COMPLETED)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CommentBean>
    ) {
        postLoadingStatus(PagedListLoadingStatus.AFTER_LOADING)
        //删除之前保存的重试方法
        retry = null
        Log.d(
            "CommentsDataSource",
            "key:${params.key} | requestedLoadSize:${params.requestedLoadSize}"
        )
        apiServices.obtainShortComments(id.toString(), commentId.toString()).callIO(
            onFailure = {
                it.printStackTrace()
                retry = { loadAfter(params, callback) }
                postLoadingStatus(PagedListLoadingStatus.AFTER_FAILED)
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    if (!it.isNullOrEmpty()) {
                        commentId = it.last().id
                        callback.onResult(it, pageNum)
                        pageNum++
                        postLoadingStatus(PagedListLoadingStatus.AFTER_LOADED)
                    } else {
                        Log.d(
                            "CommentsDataSource",
                            "全部加载完成"
                        )
                        postLoadingStatus(PagedListLoadingStatus.COMPLETED)
                    }
                }
            },
            onResponseFailure = { response ->
                Log.e(
                    "CommentsDataSource",
                    "loadAfter: ${response.errorBody()?.string() ?: "Unknown error"}"
                )
                retry = { loadAfter(params, callback) }
                postLoadingStatus(PagedListLoadingStatus.AFTER_FAILED)
            }
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CommentBean>
    ) {

    }

    //对外提供只读的成员
    private val _loadingStatus = MutableLiveData<PagedListLoadingStatus>()
    val loadingStatus: MutableLiveData<PagedListLoadingStatus>
        get() = _loadingStatus

    //向LiveData发送加载状态
    private fun postLoadingStatus(status: PagedListLoadingStatus) {
        _loadingStatus.postValue(status)
    }

    //保存当前方法用于重试
    var retry: (() -> Any?)? = null

}