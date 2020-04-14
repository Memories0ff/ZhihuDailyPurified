package com.sion.zhihudailypurified.view

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.StoriesAdapter
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityIndexBinding
import com.sion.zhihudailypurified.entity.TopStoryBean
import com.sion.zhihudailypurified.viewModel.IndexViewModel

class IndexActivity : BaseActivity<ActivityIndexBinding, IndexViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_index
    }

    override fun setViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun initView() {
        val adapter = StoriesAdapter()
        ui.rvStories.adapter = adapter
        ui.rvStories.layoutManager = LinearLayoutManager(this)
        vm.stories.observe(this, Observer(adapter::submitList))
//        vm.topStories.observe(this, Observer {
//            adapter.updateBanner()
//        })
    }

    override fun initData() {
        vm.obtainTopStories()
    }

    override fun onStart() {
        super.onStart()
        vm.bannerRunning.value = true
    }

    override fun onStop() {
        super.onStop()
        vm.bannerRunning.value = false
    }
}
