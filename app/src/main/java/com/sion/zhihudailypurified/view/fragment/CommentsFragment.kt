package com.sion.zhihudailypurified.view.fragment

import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.view.adapter.CommentsAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentCommentsBinding
import com.sion.zhihudailypurified.datasource.PagedListLoadingStatus
import com.sion.zhihudailypurified.view.itemDecoration.CommentsDecoration
import com.sion.zhihudailypurified.viewModel.fragment.CommentsViewModel

class CommentsFragment : BaseFragment<FragmentCommentsBinding, CommentsViewModel>() {

    override fun setLayoutId(): Int = R.layout.fragment_comments

    override fun setViewModel(): Class<out CommentsViewModel> = CommentsViewModel::class.java

    override fun initView() {
        vm.storyId = arguments!!.getInt(STORY_ID)   //必须先赋值id
        ui.commentsNum = ObservableField<Int>()
        ui.btnBackToContent.setOnClickListener {
            back()
        }
        vm.pagedListLoadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                PagedListLoadingStatus.INITIAL_LOADING -> {
                    Log.d("CommentsFragment", "initView: Initial loading")
                }
                PagedListLoadingStatus.INITIAL_LOADED -> {
                    Log.d("CommentsFragment", "initView: Initial loaded")
                }
                PagedListLoadingStatus.INITIAL_FAILED -> {
                    Log.d("CommentsFragment", "initView: Initial failed")
                }
                PagedListLoadingStatus.AFTER_LOADING -> {
                    Log.d("CommentsFragment", "initView: After loading")
                }
                PagedListLoadingStatus.AFTER_LOADED -> {
                    Log.d("CommentsFragment", "initView: After loaded")
                }
                PagedListLoadingStatus.AFTER_FAILED -> {
                    Log.d("CommentsFragment", "initView: After failed")
                }
                else -> {
                    Log.d("CommentsFragment", "initView: Completed")
                }
            }
        })
//        closeDefaultAnimator(ui.rvComments)         //关闭RecyclerView局部刷新动画
        vm.extraBean.observe(this, Observer {
            vm.commentsNum = it?.comments ?: 0
            vm.longCommentsNum = it?.long_comments ?: 0
            vm.shortCommentsNum = it?.short_comments ?: 0
            ui.commentsNum?.set(vm.commentsNum)
            if (it == null) {
                toast(resources.getText(R.string.extra_bean_info_loading_failed).toString())
            }
            initCommentList()
        })
        if (vm.commentsNum == 0) {
            vm.obtainExtraBean(vm.storyId)
        } else {
            initCommentList()
        }

    }

    override fun initData() {
        vm.commentsNum = arguments!!.getInt(COMMENTS_NUM)
        vm.longCommentsNum = arguments!!.getInt(LONG_COMMENTS_NUM)
        vm.shortCommentsNum = arguments!!.getInt(SHORT_COMMENTS_NUM)
        ui.commentsNum?.set(vm.commentsNum)
    }

    /**
     * 关闭RecyclerView局部刷新动画
     */
//    private fun closeDefaultAnimator(recyclerView: RecyclerView) {
//        recyclerView.itemAnimator?.apply {
//            addDuration = 0
//            changeDuration = 0
//            moveDuration = 0
//            removeDuration = 0
//            (this as SimpleItemAnimator?)?.supportsChangeAnimations = false
//        }
//    }

    private fun initCommentList() {
        val adapter =
            CommentsAdapter(this@CommentsFragment)
        ui.rvComments.layoutManager = LinearLayoutManager(this@CommentsFragment.context)
        ui.rvComments.adapter = adapter
        ui.rvComments.addItemDecoration(CommentsDecoration(this@CommentsFragment))
        vm.comments.observe(this@CommentsFragment, Observer {
            adapter.submitList(it)
        })
    }

    fun toast(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "COMMENTS_FRAGMENT"
        const val STORY_ID = "STORY_ID"
        const val COMMENTS_NUM = "COMMENTS_NUM"
        const val LONG_COMMENTS_NUM = "LONG_COMMENTS_NUM"
        const val SHORT_COMMENTS_NUM = "SHORT_COMMENTS_NUM"
    }
}