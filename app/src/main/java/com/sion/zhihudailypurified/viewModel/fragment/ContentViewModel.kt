package com.sion.zhihudailypurified.viewModel.fragment

import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.db.dbContentServices
import com.sion.zhihudailypurified.entity.StoryContentBean
import com.sion.zhihudailypurified.network.apiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentViewModel : BaseViewModel() {
    //新闻内容

    val content = MutableLiveData<StoryContentBean>()

    fun obtainStoryContent(id: Int) {
        //1、从数据库中获取内容和额外信息，没有则从网络中加载并展示
        //2、额外信息从网络中获取并更新到数据库中并展示
        GlobalScope.launch(Dispatchers.Main) {
            var isContentFromWeb = false
            val storyContent = withContext(Dispatchers.IO) {
                val contentList = dbContentServices.obtainStoryContentBeanById(id)
                if (contentList.isNullOrEmpty()) {
                    isContentFromWeb = true
                    apiServices.obtainContent(id.toString()).execute().body()
                } else {
                    isContentFromWeb = false
                    contentList[0]
                }
            } ?: return@launch

            val storyExtra = withContext(Dispatchers.IO) {
                apiServices.obtainContentExtra(id.toString()).execute().body()
            }
            storyExtra?.let { extra ->
                storyContent.extra = extra
            }
            withContext(Dispatchers.IO) {
                if (isContentFromWeb) {
                    dbContentServices.insertStoryContentBean(storyContent)
                } else {
                    if (storyExtra != null) {
                        dbContentServices.updateStoryContentBean(storyContent)
                    }
                }
            }
            content.value = storyContent
        }
    }
}