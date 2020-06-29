package com.sion.zhihudailypurified.view.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.CommentsAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentCommentsBinding
import com.sion.zhihudailypurified.view.itemDecoration.CommentsDecoration
import com.sion.zhihudailypurified.viewModel.fragment.CommentsViewModel

class CommentsFragment : BaseFragment<FragmentCommentsBinding, CommentsViewModel>() {

    override fun setLayoutId(): Int = R.layout.fragment_comments

    override fun setViewModel(): Class<out CommentsViewModel> = CommentsViewModel::class.java

    override fun initView() {

        vm.storyId = arguments!!.getInt(STORY_ID)   //必须先赋值id
        val adapter = CommentsAdapter(this@CommentsFragment)
        ui.rvComments.layoutManager = LinearLayoutManager(this@CommentsFragment.context)
        ui.rvComments.adapter = adapter
        ui.rvComments.addItemDecoration(CommentsDecoration(this@CommentsFragment))
        vm.comments.observe(this@CommentsFragment, Observer {
            adapter.submitList(it)
        })

    }

    override fun initData() {
        vm.commentsNum = arguments!!.getInt(COMMENTS_NUM)
        vm.longCommentsNum = arguments!!.getInt(LONG_COMMENTS_NUM)
        vm.shortCommentsNum = arguments!!.getInt(SHORT_COMMENTS_NUM)
        ui.commentsNum = vm.commentsNum
    }


    companion object {
        const val TAG = "COMMENTS_FRAGMENT"
        const val STORY_ID = "STORY_ID"
        const val COMMENTS_NUM = "COMMENTS_NUM"
        const val LONG_COMMENTS_NUM = "LONG_COMMENTS_NUM"
        const val SHORT_COMMENTS_NUM = "SHORT_COMMENTS_NUM"
    }
}