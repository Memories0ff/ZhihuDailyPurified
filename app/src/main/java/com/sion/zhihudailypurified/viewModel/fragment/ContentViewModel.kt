package com.sion.zhihudailypurified.viewModel.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.db.dbContentServices
import com.sion.zhihudailypurified.entity.StoryContentBean
import com.sion.zhihudailypurified.entity.StoryContentExtraBean
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
    val content = MutableLiveData<StoryContentBean?>()

    //额外信息
    val extra = MutableLiveData<StoryContentExtraBean?>()

    fun obtainStoryContentAndExtra(id: Int) {
        //1、从数据库中获取内容和额外信息，没有则从网络中加载并展示
        //2、额外信息从网络中获取并更新到数据库中并展示
        GlobalScope.launch(Dispatchers.Main) {
            var isContentFromWeb = false
            val storyContent = withContext(Dispatchers.IO) {
                val contentList = dbContentServices.obtainStoryContentBeanById(id)
                if (contentList.isNullOrEmpty()) {
                    isContentFromWeb = true
                    //TODO 超时错误未处理
                    try {
                        apiServices.obtainContent(id.toString()).execute().body()
                    } catch (e: SocketTimeoutException) {
                        Log.e("obtainStoryContent", "获取新闻内容失败：连接超时")
                        null
                    } catch (e: Exception) {
                        Log.e("obtainStoryContent", "获取新闻内容失败：未知错误")
                        null
                    }
                } else {
                    isContentFromWeb = false
                    contentList[0]
                }
            }

            val storyExtra = withContext(Dispatchers.IO) {
                try {
                    apiServices.obtainContentExtra(id.toString()).execute().body()
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                    Log.e("ObtainStoryExtra", "获取新闻其他信息失败：连接超时")
                    null
                } catch (e: Exception) {
                    Log.e("obtainStoryContent", "获取新闻其他信息失败：未知错误")
                    null
                }
            }
            withContext(Dispatchers.IO) {
                storyContent?.let {
                    if (isContentFromWeb) {
                        dbContentServices.insertStoryContentBean(storyContent)
                    } else {
                        dbContentServices.updateStoryContentBean(storyContent)
                    }
                }
            }
            //加载失败传入null
            content.value = storyContent
            extra.value = storyExtra
        }
    }

    //标记为已读
    fun markRead(displayType: Int, storyContentBean: StoryContentBean, fragment: Fragment) {
        val supportFragmentManager = fragment.requireActivity().supportFragmentManager
        if (displayType == ContentsDisplayFragment.STORIES) {
            val position = (supportFragmentManager
                .findFragmentByTag(ContentsDisplayFragment.TAG) as ContentsDisplayFragment?)
                ?.ui?.vpContents?.currentItem
            position?.let {
                (supportFragmentManager
                    .findFragmentByTag(StoriesFragment.TAG) as StoriesFragment)
                    .vm.stories.value?.get(it)?.let { storyBean ->
                        storyBean.isRead.get()?.let { it ->
                            if (!it) {
                                storyBean.isRead.set(true)
                                spPutBoolean(
                                    storyBean.id.toString(),
                                    true,
                                    fragment.requireActivity()
                                )
                            }
                        }
                    }
            }
        }
        if (displayType == ContentsDisplayFragment.TOP_STORIES) {
            (supportFragmentManager
                .findFragmentByTag(StoriesFragment.TAG) as StoriesFragment)
                .vm.stories.value?.find {
                    it.id == storyContentBean.id
                }?.isRead?.set(true)
            spPutBoolean(storyContentBean.id.toString(), true, fragment.requireActivity())
        }
    }

    //点赞数评论数更新
    fun updateExtraInfo(storyContentExtraBean: StoryContentExtraBean, fragment: Fragment) {
        (fragment.requireActivity().supportFragmentManager
            .findFragmentByTag(ContentsDisplayFragment.TAG) as ContentsDisplayFragment?)
            ?.vm?.contentExtraField?.set(storyContentExtraBean)
    }

    //更新是否收藏
    fun updateCollectionInfo(storyId: Int, fragment: Fragment) {
        (fragment.requireActivity().supportFragmentManager
            .findFragmentByTag(ContentsDisplayFragment.TAG) as ContentsDisplayFragment?)
            ?.vm?.collectionImageLiveData?.value = storyId
    }
}