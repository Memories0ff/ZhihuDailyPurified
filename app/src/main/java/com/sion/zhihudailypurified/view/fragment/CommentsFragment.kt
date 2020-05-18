package com.sion.zhihudailypurified.view.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.CommentsAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentCommentsBinding
import com.sion.zhihudailypurified.viewModel.fragment.CommentsViewModel

class CommentsFragment : BaseFragment<FragmentCommentsBinding, CommentsViewModel>() {

    private val storyId by lazy {
        arguments!!.getInt(STORY_ID)
    }

    override fun setLayoutId(): Int = R.layout.fragment_comments

    override fun setViewModel(): Class<out CommentsViewModel> = CommentsViewModel::class.java

    override fun initView() {

        ui.rvComments.apply {
            layoutManager = LinearLayoutManager(this@CommentsFragment.context)
            adapter = CommentsAdapter(this@CommentsFragment)
        }

        vm.comments.observe(this, Observer {
            ui.rvComments.adapter?.notifyDataSetChanged()
        })

    }

    override fun initData() {
        vm.obtainAllComments(storyId)
    }


    companion object {
        const val TAG = "COMMENTS_FRAGMENT"
        const val STORY_ID = "STORY_ID"
    }
}