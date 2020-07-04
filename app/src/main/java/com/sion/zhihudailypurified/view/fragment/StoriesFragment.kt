package com.sion.zhihudailypurified.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentStoriesBinding
import com.sion.zhihudailypurified.view.adapter.StoriesAdapter
import com.sion.zhihudailypurified.view.itemDecoration.DateDecoration
import com.sion.zhihudailypurified.viewModel.fragment.StoriesViewModel

class StoriesFragment : BaseFragment<FragmentStoriesBinding, StoriesViewModel>() {


    override fun setLayoutId(): Int {
        return R.layout.fragment_stories
    }

    override fun setViewModel(): Class<StoriesViewModel> {
        return StoriesViewModel::class.java
    }

    override fun initView() {
        val adapter =
            StoriesAdapter(this)
        ui.rvStories.adapter = adapter
        ui.rvStories.layoutManager = LinearLayoutManager(activity)
        ui.rvStories.addItemDecoration(DateDecoration(this))

        vm.stories.observe(this, Observer { adapter.submitList(it) })

        vm.loadTopFinished.observe(this, Observer {
            if (it) {
                vm.topStories.value?.let {
                    vm.loadTopFinished.value = false    //这步只改一个值，其他什么都不会执行
                    loadingFinish()
                }
            }
        })

        vm.lastPos.observe(this, Observer {
            adapter.smoothMoveToPosition(ui.rvStories, it + 1)
        })

    }

    private fun loadingFinish() {
        ui.llStoriesRoot.visibility = View.VISIBLE
    }


    override fun initData() {
        vm.obtainTopStories()      //读取数据
    }

    companion object {
        const val TAG = "STORIES_FRAGMENT"
    }
}