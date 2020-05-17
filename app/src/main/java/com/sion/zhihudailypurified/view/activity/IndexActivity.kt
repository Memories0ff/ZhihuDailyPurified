package com.sion.zhihudailypurified.view.activity

import androidx.lifecycle.Observer
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityIndexBinding
import com.sion.zhihudailypurified.utils.toast
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment
import com.sion.zhihudailypurified.viewModel.activity.IndexViewModel

class IndexActivity : BaseActivity<ActivityIndexBinding, IndexViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_index
    }

    override fun setViewModel(): Class<IndexViewModel> {
        return IndexViewModel::class.java
    }

    override fun initView() {
        vm.isOnline.observe(this, Observer {
            if (it) {
                toast("网络已连接")
            } else {
                toast("网络已断开")
            }
        })
        supportFragmentManager.beginTransaction()
            .add(
                R.id.flFragmentContainer,
                StoriesFragment(),
                StoriesFragment.TAG
            )
            .commit()
    }

    override fun initData() {

    }


    fun switchToContent(fragment: StoriesFragment, displayType: Int, initialPos: Int) {
        supportFragmentManager.beginTransaction()
            .hide(fragment)
            .add(
                R.id.flFragmentContainer,
                ContentsDisplayFragment(displayType, initialPos),
                ContentsDisplayFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

}
