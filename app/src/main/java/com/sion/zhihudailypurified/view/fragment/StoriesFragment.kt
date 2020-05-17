package com.sion.zhihudailypurified.view.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.StoriesAdapter
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentStoriesListBinding
import com.sion.zhihudailypurified.viewModel.fragment.StoriesViewModel

class StoriesFragment : BaseFragment<FragmentStoriesListBinding, StoriesViewModel>() {


    override fun setLayoutId(): Int {
        return R.layout.fragment_stories_list
    }

    override fun setViewModel(): Class<StoriesViewModel> {
        return StoriesViewModel::class.java
    }

    override fun initView() {
        val adapter = StoriesAdapter(this)
        ui.rvStories.adapter = adapter
        ui.rvStories.layoutManager = LinearLayoutManager(activity)

        vm.stories.observe(this, Observer { adapter.submitList(it) })
        vm.updateTopStories.observe(this, Observer {
            if (it) {
                adapter.bannerAdapter!!.notifyDataSetChanged()
            }
        })

        vm.lastPos.observe(this, Observer {
            ui.rvStories.smoothScrollToPosition(it)
        })

    }

    override fun initData() {
        vm.obtainTopStories()
    }

    override fun onStart() {
        super.onStart()
        (ui.rvStories.adapter as StoriesAdapter).banner?.start()
    }

    override fun onStop() {
        super.onStop()
        (ui.rvStories.adapter as StoriesAdapter).banner?.stop()
    }

    companion object {
        const val TAG = "STORIES_FRAGMENT"
    }
}