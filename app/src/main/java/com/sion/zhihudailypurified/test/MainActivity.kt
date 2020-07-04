package com.sion.zhihudailypurified.test

import android.util.Log
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseActivity
import com.sion.zhihudailypurified.databinding.ActivityMainBinding
import com.sion.zhihudailypurified.components.banner.BannerAdapter

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun setViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
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

//        ui.banner.indicator = CircleIndicator(this)
//        ui.banner.adapter = TopStoryBannerAdapter(vm.topStories.value!!)
//        ui.btnData.setOnClickListener {
//            vm.obtainTopStories()
//            toast("数据加载完成")
//        }
//        ui.btnUpdate.setOnClickListener {
//            ui.banner.adapter.notifyDataSetChanged()
//            toast("界面加载完成")
//        }

        val fl = findViewById<FrameLayout>(R.id.flFragmentContainer)
        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().add(R.id.flFragmentContainer, mainFragment)
            .commit()

        fl.setOnClickListener {
            Log.d("MainFragment", "单击事件fl")
        }

        vm.loadFinished.observe(this, Observer {
//            if (it) {
//                val banner = mainFragment.mBanner
//                vm.topStories.value?.let { it1 ->
//                    banner.adapter =
//                        BannerAdapter(
//                            it1,
//                            this
//                        )
//                    banner.observeFragment(mainFragment)
//                    vm.loadFinished.value = false
//                    banner.isLoadFinish(true)
//                }
//            }
        })

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
