package com.sion.zhihudailypurified.viewModel.fragment

import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.entity.CommentBean
import com.sion.zhihudailypurified.network.apiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentsViewModel : BaseViewModel() {

    var longCommentNum = 0
    var shortCommentNum = 0

    val comments = MutableLiveData<MutableList<CommentBean>>().apply {
        value = arrayListOf()
    }

    fun obtainAllComments(id: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = arrayListOf<CommentBean>()
            withContext(Dispatchers.IO) {
                apiServices.obtainLongComments(id.toString()).execute().body()
                    ?.let { commentList ->
                        longCommentNum = commentList.comments.size
                        result.addAll(commentList.comments)
                    }
            }
            withContext(Dispatchers.IO) {
                apiServices.obtainShortComments(id.toString()).execute().body()
                    ?.let { commentList ->
                        shortCommentNum = commentList.comments.size
                        result.addAll(commentList.comments)
                    }
            }
            comments.value = result
        }
    }
}