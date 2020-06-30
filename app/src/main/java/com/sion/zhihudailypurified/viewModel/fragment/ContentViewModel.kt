package com.sion.zhihudailypurified.viewModel.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.db.dbContentServices
import com.sion.zhihudailypurified.entity.StoryContentBean
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.sharedPreference.spPutBoolean
import com.sion.zhihudailypurified.view.fragment.ContentsDisplayFragment
import com.sion.zhihudailypurified.view.fragment.StoriesFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

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
                    //???????????????????????超时错误
                    try {
                        apiServices.obtainContent(id.toString()).execute().body()
                    } catch (e: SocketTimeoutException) {
                        Log.e("obtainStoryContent", "获取新闻内容失败")
                        null
                    }
                } else {
                    isContentFromWeb = false
                    contentList[0]
                }
            } ?: return@launch

            val storyExtra = withContext(Dispatchers.IO) {
                try {
                    apiServices.obtainContentExtra(id.toString()).execute().body()
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                    Log.e("ObtainStoryExtra", "timeout")
                    null
                }
            }
            storyExtra?.let { extra ->
                storyContent.extra = extra
            } ?: return@launch
            withContext(Dispatchers.IO) {
                if (isContentFromWeb) {
                    dbContentServices.insertStoryContentBean(storyContent)
                } else {
                    dbContentServices.updateStoryContentBean(storyContent)
                }
            }
            content.value = storyContent
        }
    }

    //标记为已读
    fun markRead(displayType: Int, storyContentBean: StoryContentBean, fragment: Fragment) {
        if (displayType == ContentsDisplayFragment.STORIES) {
            val position = (fragment.activity!!.supportFragmentManager
                .findFragmentByTag(ContentsDisplayFragment.TAG) as ContentsDisplayFragment?)
                ?.ui?.vpContents?.currentItem
            position?.let {
                (fragment.activity!!.supportFragmentManager
                    .findFragmentByTag(StoriesFragment.TAG) as StoriesFragment)
                    .vm.stories.value!![it]!!.let {
                    if (!it.isRead.get()!!) {
                        it.isRead.set(true)
                        spPutBoolean(it.id.toString(), true, fragment.requireActivity())
                    }
                }
            }
        }
        if (displayType == ContentsDisplayFragment.TOP_STORIES) {
            (fragment.activity!!.supportFragmentManager
                .findFragmentByTag(StoriesFragment.TAG) as StoriesFragment)
                .vm.stories.value!!.find {
                    it.id == storyContentBean.id
                }?.isRead?.set(true)

            spPutBoolean(storyContentBean.id.toString(), true, fragment.requireActivity())
        }
    }

    //点赞数评论数更新
    fun updateExtraInfo(storyContentBean: StoryContentBean, fragment: Fragment) {
        (fragment.activity!!.supportFragmentManager
            .findFragmentByTag(ContentsDisplayFragment.TAG) as ContentsDisplayFragment?)
            ?.vm?.contentExtraField?.set(storyContentBean.extra)
    }
}