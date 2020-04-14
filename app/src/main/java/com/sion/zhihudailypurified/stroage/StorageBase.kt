package com.sion.zhihudailypurified.stroage

import android.os.Environment
import android.util.Log
import com.sion.zhihudailypurified.App
import java.io.File

val appFileSeparator by lazy {
    File.separator
}

/**
 * 图片缓存目录字符串，无法获取图片缓存目录则返回空
 */
val appPicCacheDirectoryPath by lazy {
    if (
        Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        || !Environment.isExternalStorageRemovable()
    ) {
        App.getAppContext().externalCacheDir?.let {
            it.absolutePath + appFileSeparator + "picCache"
        } ?: (App.getAppContext().cacheDir.absolutePath + appFileSeparator + "picCache")
    } else {
        App.getAppContext().cacheDir.absolutePath + appFileSeparator + "picCache"
    }
}

fun mkCacheDir() {

    val picCacheDirectory = File(appPicCacheDirectoryPath)

    if (!picCacheDirectory.isDirectory) {
        picCacheDirectory.delete()
    }

    if (!picCacheDirectory.exists()) {
        if (!picCacheDirectory.mkdir()) {
            Log.e("StorageBase", "建立缓存文件夹失败")
        }
    }
}

fun cacheDirExists(): Boolean {
    return File(appPicCacheDirectoryPath).exists()
}