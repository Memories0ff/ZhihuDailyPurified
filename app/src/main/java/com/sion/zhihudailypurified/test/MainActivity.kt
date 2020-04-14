package com.sion.zhihudailypurified.test

import androidx.lifecycle.Observer
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun setViewModel(): MainViewModel {
        return MainViewModel()
    }

    override fun initView() {

        ui.btnDownload.setOnClickListener {
            vm.obtainPic()
        }

        ui.btnSave.setOnClickListener {
            vm.savePicCache()
        }

        ui.btnClear.setOnClickListener {
            vm.clearCache()
        }
    }

    override fun initData() {

        vm.content.observe(this, Observer {
            ui.content = it
        })
        vm.bitmap.observe(this, Observer {
            ui.ivImage.setImageBitmap(it)
        })

        vm.obtainPast(1)
//        vm.obtainLatest()
    }
}
