package com.sion.zhihudailypurified.view.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.CommentsAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentCommentsBinding
import com.sion.zhihudailypurified.viewModel.fragment.CommentsViewModel

class CommentsFragment : BaseFragment<FragmentCommentsBinding, CommentsViewModel>() {

    override fun setLayoutId(): Int = R.layout.fragment_comments

    override fun setViewModel(): Class<out CommentsViewModel> = CommentsViewModel::class.java

    override fun initView() {

        ui.rvComments.apply {
            layoutManager = LinearLayoutManager(this@CommentsFragment.context)
            adapter = CommentsAdapter(this@CommentsFragment)
        }

        vm.comments.observe(this, Observer {
            ui.commentsNum = vm.commentsNum
            ui.rvComments.adapter?.notifyDataSetChanged()
        })

    }

    override fun initData() {
        vm.storyId = arguments!!.getInt(STORY_ID)
        vm.commentsNum = arguments!!.getInt(COMMENTS_NUM)
        vm.longCommentsNum = arguments!!.getInt(LONG_COMMENTS_NUM)
        vm.shortCommentsNum = arguments!!.getInt(SHORT_COMMENTS_NUM)
        vm.obtainAllComments(vm.storyId)
    }


    companion object {
        const val TAG = "COMMENTS_FRAGMENT"
        const val STORY_ID = "STORY_ID"
        const val COMMENTS_NUM = "COMMENTS_NUM"
        const val LONG_COMMENTS_NUM = "LONG_COMMENTS_NUM"
        const val SHORT_COMMENTS_NUM = "SHORT_COMMENTS_NUM"
    }
}