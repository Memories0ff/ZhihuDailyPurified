package com.sion.zhihudailypurified.viewModel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.datasource.CommentsDataSourceFactory
import com.sion.zhihudailypurified.entity.CommentBean
import com.sion.zhihudailypurified.network.apiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentsViewModel : BaseViewModel() {

    var storyId = 0
    var commentsNum = 0
    var longCommentsNum = 0
    var shortCommentsNum = 0

//    var currentLongCommentNum = 0
//    var currentShortCommentNum = 0

    val comments by lazy {
        LivePagedListBuilder(
            CommentsDataSourceFactory(storyId),
            PagedList.Config.Builder()
                .setPageSize(35)
                .setEnablePlaceholders(false)
                .build()
        ).build()
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