package com.sion.zhihudailypurified.view.fragment

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
                update()
                hideError()
            }
        }
    }


    override fun setLayoutId(): Int {
        return R.layout.fragment_stories
    }

    override fun setViewModel(): Class<StoriesViewModel> {
        return StoriesViewModel::class.java
    }

    override fun initView() {

        vm.pagedListLoadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                PagedListLoadingStatus.INITIAL_LOADING -> {
                    Log.d("StoriesFragment", "initView: Initial loading")
                }
                PagedListLoadingStatus.INITIAL_LOADED -> {
                    Log.d("StoriesFragment", "initView: Initial loaded")
                    //今日stories加载完再显示界面
                    initialLoadingFinish()
                }
                PagedListLoadingStatus.INITIAL_FAILED -> {
                    Log.d("StoriesFragment", "initView: Initial failed")
                    showError()
                }
                PagedListLoadingStatus.AFTER_LOADING -> {
                    Log.d("StoriesFragment", "initView: After loading")
                }
                PagedListLoadingStatus.AFTER_LOADED -> {
                    Log.d("StoriesFragment", "initView: After loaded")
                }
                PagedListLoadingStatus.AFTER_FAILED -> {
                    Log.d("StoriesFragment", "initView: After failed")
                }
                else -> {
                    Log.d("StoriesFragment", "initView: Completed")
                }
            }
        })
        vm.topStoriesLoadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                TopStoriesLoadingStatus.LOADED -> {
                    //保证加载完top再加载今日stories
                    val adapter = StoriesAdapter(this)
                    ui.rvStories.adapter = adapter
                    ui.rvStories.layoutManager = LinearLayoutManager(activity)
                    ui.rvStories.addItemDecoration(DateDecoration(this))

                    //与变量adapter关联的操作
                    //保证先初始化stories列表再执行这些操作
                    vm.stories.observe(this, Observer { adapter.submitList(it) })
                    vm.lastPos.observe(this, Observer {
                        adapter.smoothMoveToPosition(ui.rvStories, it + 1)
                    })
                }
                TopStoriesLoadingStatus.FAILED -> {
                    showError()
                }
                else -> {

                }
            }
        })
    }

    //初始加载完毕
    private fun initialLoadingFinish() {
        //显示界面
        ui.llStoriesRoot.visibility = View.VISIBLE
    }


    override fun initData() {
        vm.obtainTopStories()      //读取数据
    }

    //更新
    private fun update() {
        vm.updateData()
    }

    //显示和隐藏错误信息
    private fun showError() {
        errorUI.visibility = View.VISIBLE
    }

    private fun hideError() {
        errorUI.visibility = View.GONE
    }

    companion object {
        const val TAG = "STORIES_FRAGMENT"
    }
}