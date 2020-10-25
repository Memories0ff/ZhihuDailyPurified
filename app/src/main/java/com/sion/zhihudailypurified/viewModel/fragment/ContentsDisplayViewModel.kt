package com.sion.zhihudailypurified.viewModel.fragment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.entity.StoryContentExtraBean
import com.tencent.mmkv.MMKV

class ContentsDisplayViewModel : BaseViewModel() {
    //用于额外信息显示和更新
    val contentExtraField = ObservableField<StoryContentExtraBean>()

    /**
     * 收藏
     */
    //用于更新收藏图标
    val collectionImageField = ObservableField<Boolean>()
    val collectionImageLiveData = MutableLiveData<Int>()

    private val collectedMMKV = MMKV.mmkvWithID(COLLECTED_MMKV)

    //添加收藏
    fun insertCollection(storyId: Int) {
        collectedMMKV.encode(storyId.toString(), true)
    }

    //查询收藏（存在返回true）
    fun queryCollection(storyId: Int): Boolean {
        return collectedMMKV.decodeBool(storyId.toString(), false)
    }

    //删除收藏
    fun removeCollection(storyId: Int) {
        collectedMMKV.removeValueForKey(storyId.toString())
    }

    /**
     * 点赞
     */
    //点赞图标
    val likeImageField = ObservableField<Boolean>()
    val likeImageLiveData = MutableLiveData<Int>()

    //点赞数
//    val likeNumTxtField = ObservableField<Int>()

    private val likedMMKV = MMKV.mmkvWithID(LIKED_MMKV);

    //添加点赞
    fun insertLike(storyId: Int) {
        likedMMKV.encode(storyId.toString(), true)
    }

    //查询点赞（存在返回true）
    fun queryLike(storyId: Int): Boolean {
        return likedMMKV.decodeBool(storyId.toString(), false)
    }

    //删除点赞
    fun removeLike(storyId: Int) {
        likedMMKV.removeValueForKey(storyId.toString())
    }

    companion object {
        const val COLLECTED_MMKV = "COLLECTED_MMKV"
        const val LIKED_MMKV = "LIKED_MMKV"
    }
}