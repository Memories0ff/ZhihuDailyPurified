package com.sion.zhihudailypurified.view.fragment

import androidx.viewpager2.widget.ViewPager2
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.base.BaseFragment
import com.sion.zhihudailypurified.databinding.FragmentGalleryBinding
import com.sion.zhihudailypurified.view.adapter.GalleryVPAdapter
import com.sion.zhihudailypurified.viewModel.fragment.ImagesGalleryViewModel

class GalleryFragment : BaseFragment<FragmentGalleryBinding, ImagesGalleryViewModel>() {

    override fun setLayoutId(): Int = R.layout.fragment_gallery

    override fun setViewModel(): Class<out ImagesGalleryViewModel> =
        ImagesGalleryViewModel::class.java

    override fun initView() {
        vm.currentIndex = arguments!!.getInt(INITIAL_INDEX)
        vm.picNum = arguments!!.getInt(PIC_NUM)
        vm.picUrls = arguments!!.getStringArray(PIC_URLS) as Array<String>
        ui.tvNumIndication.text =
            getString(R.string.images_gallery_indication, vm.currentIndex + 1, vm.picNum)
        ui.vpGallery.adapter = GalleryVPAdapter(this, vm)
        ui.vpGallery.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                ui.tvNumIndication.text =
                    getString(R.string.images_gallery_indication, position + 1, vm.picNum)
            }
        })
        ui.vpGallery.setCurrentItem(vm.currentIndex, false)
    }

    override fun initData() {

    }

    companion object {
        const val TAG = "IMAGES_GALLERY_FRAGMENT"
        const val INITIAL_INDEX = "INITIAL_INDEX"
        const val PIC_NUM = "PIC_NUM"
        const val PIC_URLS = "PIC_URLS"
    }

}