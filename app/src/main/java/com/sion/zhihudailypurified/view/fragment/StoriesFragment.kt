package com.sion.zhihudailypurified.view.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.view.adapter.StoriesAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentStoriesListBinding
import com.sion.zhihudailypurified.view.itemDecoration.DateDecoration
import com.sion.zhihudailypurified.viewModel.fragment.StoriesViewModel

class StoriesFragment : BaseFragment<FragmentStoriesListBinding, StoriesViewModel>() {


    override fun setLayoutId(): Int {
        return R.layout.fragment_stories_list
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
                    //????????????????????屏幕关闭时加载完成出错
                    adapter.banner!!.addObserver(this)
                    vm.loadTopFinished.value = false    //这步什么都不会执行
                    adapter.notifyDataSetChanged()
                    adapter.banner!!.isLoadFinish(true)     //读取top stories完成，显示内容
                }
            }
        })

        vm.lastPos.observe(this, Observer {
            adapter.smoothMoveToPosition(ui.rvStories, it + 1)
        })

    }

    override fun initData() {
        vm.obtainTopStories()
    }

    companion object {
        const val TAG = "STORIES_FRAGMENT"
    }
}