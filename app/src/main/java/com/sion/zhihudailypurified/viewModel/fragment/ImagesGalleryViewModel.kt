package com.sion.zhihudailypurified.viewModel.fragment

import com.sion.zhihudailypurified.base.BaseViewModel

class ImagesGalleryViewModel : BaseViewModel() {
    var currentIndex = 0
    var picNum = 0
    var picUrls = arrayOf<String>()
}