package com.sion.zhihudailypurified.view.fragment

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentStoriesBinding
import com.sion.zhihudailypurified.datasource.PagedListLoadingStatus
import com.sion.zhihudailypurified.view.adapter.StoriesAdapter
import com.sion.zhihudailypurified.view.itemDecoration.DateDecoration
import com.sion.zhihudailypurified.viewModel.fragment.StoriesViewModel
import com.sion.zhihudailypurified.viewModel.fragment.TopStoriesLoadingStatus

class StoriesFragment : BaseFragment<FragmentStoriesBinding, StoriesViewModel>() {

    //出错界面
    private val errorUI: LinearLayout by lazy {
        ui.vsError.viewStub!!.inflate().findViewById<LinearLayout>(R.id.llClickRetry).apply {
            setOnClickListener {
                hideError()
                update()
            }
        }
    }

    private val adapter by lazy {
        StoriesAdapter(this, PagedListLoadingStatus.INITIAL_LOADING, vm)
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_stories
    }

    override fun setViewModel(): Class<StoriesViewModel> {
        return StoriesViewModel::class.java
    }

    override fun initView() {
        vm.stories.observe(
            viewLifecycleOwner,
            Observer { t -> adapter.submitList(t) }
        )
        vm.lastPos.observe(viewLifecycleOwner, Observer {
            Log.d("lastPos.observe", "initView: ${it + 1}")
            adapter.smoothMoveToPosition(ui.rvStories, it + 1)
        })
        vm.pagedListLoadingStatus.observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingStatus(it)
            when (it) {
                PagedListLoadingStatus.INITIAL_LOADING -> {
//                    Log.d("StoriesFragment", "initView: Initial loading")
                }
                PagedListLoadingStatus.INITIAL_LOADED -> {
//                    Log.d("StoriesFragment", "initView: Initial loaded")
                    //今日stories加载完再显示界面
                    initialLoadingFinish()
                }
                PagedListLoadingStatus.INITIAL_FAILED -> {
//                    Log.d("StoriesFragment", "initView: Initial failed")
                    showError()
                }
                PagedListLoadingStatus.AFTER_LOADING -> {
                    Log.d("StoriesFragment", "initView: After loading")
                }
                PagedListLoadingStatus.AFTER_LOADED -> {
                    //TODO 刷新时不应该被调用一次
                    Log.d("StoriesFragment", "initView: After loaded")
                }
                PagedListLoadingStatus.AFTER_FAILED -> {
//                    Log.d("StoriesFragment", "initView: After failed")
                }
                else -> {
//                    Log.d("StoriesFragment", "initView: Completed")
                }
            }
        })
        vm.topStoriesLoadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                TopStoriesLoadingStatus.LOADED -> {
                    //保证加载完top再加载今日stories
//                    val adapter = StoriesAdapter(this, PagedListLoadingStatus.INITIAL_LOADING, vm)
//                    this@StoriesFragment.adapter = adapter
                    removeAllDecorations(ui.rvStories)
                    ui.rvStories.adapter = adapter
                    ui.rvStories.layoutManager = LinearLayoutManager(activity)
                    ui.rvStories.addItemDecoration(DateDecoration(this))

                    //与变量adapter关联的操作
                    //保证先初始化stories列表再执行这些操作
                }
                TopStoriesLoadingStatus.FAILED -> {
                    showError()
                }
                else -> {

                }
            }
        })
        ui.srlIndexRefresh.setOnRefreshListener {
            if (errorUI.visibility == View.VISIBLE) {
                hideError()
            }
            update()
        }
    }

    override fun onHide() {
        //隐藏时banner停止滚动
        Log.d("Banner", "onHide")
        adapter.banner?.stopRolling()
    }

    override fun onShow() {
        //显示时banner继续滚动
        Log.d("Banner", "onShow")
        adapter.banner?.startRolling()
    }

    //删除RecyclerView所有decoration，防止decoration随着刷新间距变大
    private fun removeAllDecorations(recyclerView: RecyclerView) {
        recyclerView.invalidateItemDecorations()
        for (n in 0 until recyclerView.itemDecorationCount) {
            recyclerView.removeItemDecorationAt(0)
        }
    }

//    //关闭默认局部刷新动画
//    private fun closeDefaultAnimator(recyclerView: RecyclerView) {
//        recyclerView.itemAnimator?.addDuration = 0
//        recyclerView.itemAnimator?.changeDuration = 0
//        recyclerView.itemAnimator?.moveDuration = 0
//        recyclerView.itemAnimator?.removeDuration = 0
//        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
//    }

    //初始加载完毕
    private fun initialLoadingFinish() {
        //显示界面
        ui.srlIndexRefresh.isRefreshing = false
        ui.llStoriesRoot.visibility = View.VISIBLE
        ui.rvStories.visibility = View.VISIBLE
    }


    override fun initData() {
        vm.obtainTopStories()      //读取数据
    }

    //更新
    private fun update() {
        adapter.banner?.stopRolling()
        ui.rvStories.visibility = View.GONE
        ui.llStoriesRoot.visibility = View.GONE
        vm.updateData()
    }

    //显示和隐藏错误信息
    private fun showError() {
        ui.srlIndexRefresh.isRefreshing = false
        errorUI.visibility = View.VISIBLE
    }

    private fun hideError() {
        ui.srlIndexRefresh.isRefreshing = true
        errorUI.visibility = View.GONE
    }

    companion object {
        const val TAG = "STORIES_FRAGMENT"
    }
}