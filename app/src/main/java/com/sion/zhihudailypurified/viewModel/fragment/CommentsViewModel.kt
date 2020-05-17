package com.sion.zhihudailypurified.viewModel.fragment

import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.entity.CommentBean

class CommentsViewModel : BaseViewModel() {

    val longComments = MutableLiveData<List<CommentBean>>().apply {
        value = arrayListOf()
    }
    val shortComments = MutableLiveData<List<CommentBean>>().apply {
        value = arrayListOf()
    }

}