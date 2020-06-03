package com.sion.zhihudailypurified.viewModel.fragment

import androidx.databinding.ObservableField
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.entity.StoryContentExtraBean

class ContentsDisplayViewModel : BaseViewModel() {
    val contentExtraField= ObservableField<StoryContentExtraBean>()
}