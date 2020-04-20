package com.sion.zhihudailypurified.view

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.StoriesAdapter
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityIndexBinding
import com.sion.zhihudailypurified.utils.logi
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
        vm.updateTopStories.observe(this, Observer {
            if (it) {
                adapter.bannerAdapter!!.notifyDataSetChanged()
            }
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
}
