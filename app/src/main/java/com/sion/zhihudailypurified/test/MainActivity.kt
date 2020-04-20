package com.sion.zhihudailypurified.test

import androidx.lifecycle.Observer
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.adapter.TopStoryBannerAdapter
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityMainBinding
import com.sion.zhihudailypurified.utils.toast
import com.youth.banner.indicator.CircleIndicator

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun setViewModel(): MainViewModel {
        return MainViewModel()
    }

    override fun initView() {

//        ui.btnDownload.setOnClickListener {
//            vm.obtainPic()
//        }
//
//        ui.btnSave.setOnClickListener {
//            vm.savePicCache()
//        }
//
//        ui.btnClear.setOnClickListener {
//            vm.clearCache()
//        }

        ui.banner.indicator = CircleIndicator(this)
        ui.banner.adapter = TopStoryBannerAdapter(vm.topStories.value!!)
        ui.btnData.setOnClickListener {
            vm.obtainTopStories()
            toast("数据加载完成")
        }
        ui.btnUpdate.setOnClickListener {
            ui.banner.adapter.notifyDataSetChanged()
            toast("界面加载完成")
        }
    }

    override fun initData() {

//        vm.content.observe(this, Observer {
//            ui.content = it
//        })
//        vm.bitmap.observe(this, Observer {
//            ui.ivImage.setImageBitmap(it)
//        })
//
//        vm.obtainPast(1)
//        vm.obtainLatest()


    }
}
