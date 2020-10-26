package com.sion.zhihudailypurified.viewModel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.datasource.CommentsDataSourceFactory
import com.sion.zhihudailypurified.entity.StoryContentExtraBean
import com.sion.zhihudailypurified.network.apiServices
import com.tencent.mmkv.MMKV
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsViewModel : BaseViewModel() {

    var storyId = 0
    var commentsNum = 0
    var longCommentsNum = 0
    var shortCommentsNum = 0

    val extraBean by lazy {
        //加载失败传入null
        MutableLiveData<StoryContentExtraBean?>()
    }

    //    var currentLongCommentNum = 0
    //    var currentShortCommentNum = 0
    private val factory by lazy { CommentsDataSourceFactory(storyId) }

    val pagedListLoadingStatus by lazy {
        Transformations.switchMap(factory.dataSource) { it.loadingStatus }
    }

    val comments by lazy {
        LivePagedListBuilder(
            factory,
            PagedList.Config.Builder()
                .setPageSize(35)
                .setEnablePlaceholders(false)
                .build()
        ).build()
    }

    fun obtainExtraBean(id: Int) {
        apiServices.obtainContentExtra(id.toString()).enqueue(object :
            Callback<StoryContentExtraBean?> {
            override fun onResponse(
                call: Call<StoryContentExtraBean?>,
                response: Response<StoryContentExtraBean?>
            ) {
                if (response.isSuccessful) {
                    extraBean.value = response.body()
                } else {
                    extraBean.value = null
                }
            }

            override fun onFailure(call: Call<StoryContentExtraBean?>, t: Throwable) {
                extraBean.value = null
            }
        })
    }

    //刷新评论列表
    fun updateData() {
        obtainExtraBean(storyId)
        factory.dataSource.value?.invalidate()
    }

    //列表加载失败的重试
    fun retry() {
        factory.dataSource.value?.retry?.invoke()
    }

    /**
     * 评论点赞操作
     */
    private val commentMMKV = MMKV.mmkvWithID(COMMENT_MMKV)

    //查询评论点赞信息
    fun queryCommentLike(userId: Int, time: Long): Boolean {
        val key = userId.toString() + time.toString()
        return commentMMKV.decodeBool(key, false)
    }

    fun insertCommentLike(userId: Int, time: Long) {
        val key = userId.toString() + time.toString()
        commentMMKV.encode(key, true)
    }

    fun removeCommentLike(userId: Int, time: Long) {
        val key = userId.toString() + time.toString()
        commentMMKV.removeValueForKey(key)
    }

    companion object {
        const val COMMENT_MMKV = "COMMENT_MMKV"
    }

//    fun obtainAllComments(id: Int) {
//        GlobalScope.launch(Dispatchers.Main) {
//            val result = arrayListOf<CommentBean>()
//            withContext(Dispatchers.IO) {
//                apiServices.obtainLongComments(id.toString()).execute().body()
//                    ?.let { commentList ->
//                        currentLongCommentNum = commentList.comments.size
//                        result.addAll(commentList.comments)
//                    }
//            }
//            withContext(Dispatchers.IO) {
//                apiServices.obtainShortComments(id.toString()).execute().body()
//                    ?.let { commentList ->
//                        currentShortCommentNum = commentList.comments.size
//                        result.addAll(commentList.comments)
//                    }
//            }
//            comments.value = result
//        }
//    }
}