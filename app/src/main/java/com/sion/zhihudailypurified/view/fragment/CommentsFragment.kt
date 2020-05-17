package com.sion.zhihudailypurified.view.fragment

import androidx.lifecycle.Observer
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentCommentsBinding
import com.sion.zhihudailypurified.viewModel.fragment.CommentsViewModel

class CommentsFragment : BaseFragment<FragmentCommentsBinding, CommentsViewModel>() {

    override fun setLayoutId(): Int = R.layout.fragment_comments

    override fun setViewModel(): Class<out CommentsViewModel> = CommentsViewModel::class.java

    override fun initView() {

        vm.longComments.observe(this, Observer {

        })

        vm.shortComments.observe(this, Observer {

        })

    }

    override fun initData() {

    }
}