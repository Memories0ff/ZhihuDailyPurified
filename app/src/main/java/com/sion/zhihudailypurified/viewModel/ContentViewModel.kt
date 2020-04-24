package com.sion.zhihudailypurified.viewModel

import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.entity.StoryContentBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO

class ContentViewModel(var id: Int) : BaseViewModel() {

    val content = MutableLiveData<StoryContentBean>()

    fun obtainStoryContent() {
        apiServices.obtainContent(id.toString()).callIO(
            onFailure = {
                it.printStackTrace()
            },
            onResponseFailure = {

            },
            onResponseSuccess = { response ->
                content.value = response.body()
            }
        )
    }

}