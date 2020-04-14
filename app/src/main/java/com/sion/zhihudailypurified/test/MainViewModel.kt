package com.sion.zhihudailypurified.test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import com.sion.zhihudailypurified.base.BaseViewModel
import com.sion.zhihudailypurified.network.apiServices
import com.sion.zhihudailypurified.network.callIO
import com.sion.zhihudailypurified.stroage.savePicCache
import com.sion.zhihudailypurified.utils.obtainDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class MainViewModel : BaseViewModel() {
    val content: MutableLiveData<String> by lazy {
        val t = MutableLiveData<String>()
        t
    }

    val bitmap: MutableLiveData<Bitmap> by lazy {
        val t = MutableLiveData<Bitmap>()
        t
    }

    fun obtainPic() {
        apiServices.obtainPic("https://mijisou.com/themes/entropage/img/logo_index_zh_CN.png")
            .callIO(
                onFailure = {
                    it.printStackTrace()
                    bitmap.value = null
                },
                onResponseFailure = { _ ->
                    bitmap.value = null
                },
                onResponseSuccess = { response ->
                    bitmap.value = BitmapFactory.decodeStream(response.body()?.byteStream())
                }
            )
    }

    fun obtainLatest() {
        apiServices.obtainLatest().callIO(
            onFailure = {
                it.printStackTrace()
            },
            onResponseFailure = { _ ->
                toast("下载内容失败")
            },
            onResponseSuccess = { response ->
                response.body()?.let {
                    content.value = it.date
                }
            }
        )
    }

    fun obtainPast(pastDays: Int) {
        if (pastDays <= 0) {
            throw Exception("过去的天数必须为正数")
        }
        apiServices.obtainPastNews(obtainDay(Date(), -pastDays + 1)).callIO(
            onFailure = {
                it.printStackTrace()
            },
            onResponseFailure = { _ ->
                toast("下载内容失败")
            },
            onResponseSuccess = { response ->
                response.body()?.let { pastStroies ->
                    pastStroies.stories.forEach { storyBean ->
                        storyBean.date = pastStroies.date
                    }
                    content.value = pastStroies.date
                }
            }
        )
    }

    fun savePicCache() {
        GlobalScope.launch(Dispatchers.Main) {
            bitmap.value?.let {
                if (savePicCache(it)) {
                    toast("保存成功")
                } else {
                    toast("保存失败")
                }
            }
        }
    }

    fun clearCache() {
        GlobalScope.launch(Dispatchers.Main) {
            if (com.sion.zhihudailypurified.stroage.clearCache()) {
                toast("清理缓存成功")
            } else {
                toast("清理缓存失败")
            }
        }
    }
}