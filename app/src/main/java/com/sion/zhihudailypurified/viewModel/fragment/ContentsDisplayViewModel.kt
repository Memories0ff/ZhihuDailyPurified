package com.sion.zhihudailypurified.viewModel.fragment

import androidx.databinding.ObservableField
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.entity.StoryContentExtraBean

class ContentsDisplayViewModel : BaseViewModel() {
    //用于额外信息显示和更新
    val contentExtraField = ObservableField<StoryContentExtraBean>()
}