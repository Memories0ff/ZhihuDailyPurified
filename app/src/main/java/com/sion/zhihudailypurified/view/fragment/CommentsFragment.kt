package com.sion.zhihudailypurified.view.fragment

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentCommentsBinding
import com.sion.zhihudailypurified.datasource.PagedListLoadingStatus
import com.sion.zhihudailypurified.view.adapter.CommentsAdapter
import com.sion.zhihudailypurified.view.itemDecoration.CommentsDecoration
import com.sion.zhihudailypurified.viewModel.fragment.CommentsViewModel

class CommentsFragment : BaseFragment<FragmentCommentsBinding, CommentsViewModel>() {

    //出错界面
    private val errorUI by lazy {
        ui.vsError.viewStub!!.inflate().findViewById<LinearLayout>(R.id.llClickRetry).apply {
            setOnClickListener {
                update()
                hideError()
            }
        }
    }

    override fun setLayoutId(): Int = R.layout.fragment_comments

    override fun setViewModel(): Class<out CommentsViewModel> = CommentsViewModel::class.java

    override fun initView() {
        vm.storyId = arguments!!.getInt(STORY_ID)   //必须先赋值id
        ui.commentsNum = ObservableField<Int>()
        ui.btnBackToContent.setOnClickListener {
            back()
        }
//        closeDefaultAnimator(ui.rvComments)         //关闭RecyclerView局部刷新动画
        vm.extraBean.observe(viewLifecycleOwner, Observer {
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

    //初始化CommentList的辅助方法
    private fun initCommentList() {
        val adapter =
            CommentsAdapter(this@CommentsFragment, PagedListLoadingStatus.INITIAL_LOADING, vm)
        vm.pagedListLoadingStatus.observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingStatus(it)
            when (it) {
                PagedListLoadingStatus.INITIAL_LOADING -> {
                    Log.d("CommentsFragment", "initView: Initial loading")
                }
                PagedListLoadingStatus.INITIAL_LOADED -> {
                    Log.d("CommentsFragment", "initView: Initial loaded")
                    initialLoadingFinish()
                }
                PagedListLoadingStatus.INITIAL_FAILED -> {
                    Log.d("CommentsFragment", "initView: Initial failed")
                    showError()
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
        ui.rvComments.layoutManager = LinearLayoutManager(this@CommentsFragment.context)
        ui.rvComments.adapter = adapter
        ui.rvComments.addItemDecoration(CommentsDecoration(this@CommentsFragment))
        vm.comments.observe(this@CommentsFragment.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    fun toast(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    //初始加载完毕
    private fun initialLoadingFinish() {
        //显示界面
        ui.rvComments.visibility = View.VISIBLE
    }

    private fun showError() {
        errorUI.visibility = View.VISIBLE
    }

    private fun hideError() {
        errorUI.visibility = View.GONE
    }

    private fun update() {
        vm.updateData()
    }

    companion object {
        const val TAG = "COMMENTS_FRAGMENT"
        const val STORY_ID = "STORY_ID"
        const val COMMENTS_NUM = "COMMENTS_NUM"
        const val LONG_COMMENTS_NUM = "LONG_COMMENTS_NUM"
        const val SHORT_COMMENTS_NUM = "SHORT_COMMENTS_NUM"
    }
}