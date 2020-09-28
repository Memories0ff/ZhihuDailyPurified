package com.sion.zhihudailypurified.stroage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun savePicCache(cache: Bitmap): Boolean {
    var result = false
    withContext(Dispatchers.IO) {
        try {
            if (!cacheDirExists()) {
                mkCacheDir()
                if (!cacheDirExists()) {
                    return@withContext
                }
            }
            val file = File(appPicCacheDirectoryPath + appFileSeparator + "downloadedPic.png")
            val outputStream = file.outputStream()
            cache.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            result = true
        } catch (e: Exception) {
            result = false
            e.printStackTrace()
        }
    }
    return result
}

suspend fun loadPicCache(cacheName: String): Bitmap? {
    var r: Bitmap? = null
    withContext(Dispatchers.IO) {
        if (!cacheDirExists()) {
            mkCacheDir()
            if (!cacheDirExists()) {
                return@withContext
            }
        }
        val file = File(appPicCacheDirectoryPath + appFileSeparator + cacheName)
        if (file.exists()) {
            r = BitmapFactory.decodeFile(file.absolutePath)
        }
    }
    return r
}

suspend fun clearCache(): Boolean {
    var result = true
    withContext(Dispatchers.IO) {
        if (!cacheDirExists()) {
            mkCacheDir()
            if (!cacheDirExists()) {
                result = false
                return@withContext
            }
        }
        val directory = File(appPicCacheDirectoryPath)
        directory.listFiles()?.forEach { file ->
            if (
                !if (!file.isDirectory) {
                    file.delete()
                } else {
                    file.deleteRecursively()
                }
            ) {
                result = false
            }
        }
    }
    return result
}