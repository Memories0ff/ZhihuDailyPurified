package com.sion.zhihudailypurified.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.net.SocketTimeoutException

fun <T> Call<T>.callIO(
    onFailure: ((t: Throwable) -> Unit)? = null,
    onResponseFailure: ((response: Response<T>) -> Unit)? = null,
    onResponseSuccess: ((response: Response<T>) -> Unit)? = null
) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("DownloadUtils", "请求错误")
            onFailure?.invoke(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onResponseSuccess?.invoke(response)
            } else {
                Log.e("DownloadUtils", "响应错误")
                onResponseFailure?.invoke(response)
            }
        }
    })

//    GlobalScope.launch(Dispatchers.Main) {
//        withContext(Dispatchers.IO) {
//            try {
//                //????????????????????可能的异常没有处理　
//                execute()
//            } catch (e: SocketTimeoutException) {
//                Log.e("DownloadUtils", "连接超时")
//                onFailure?.invoke(e)
//                null
//            } catch (e: IOException) {
//                Log.e("DownloadUtils", "IO错误")
//                onFailure?.invoke(e)
//                null
//            } catch (e: RuntimeException) {
//                Log.e("DownloadUtils", "运行时错误")
//                onFailure?.invoke(e)
//                null
//            } catch (e: Exception) {
//                Log.e("DownloadUtils", "未知错误")
//                onFailure?.invoke(e)
//                null
//            }
//        }?.let { response ->
//            if (response.isSuccessful) {
//                onResponseSuccess?.invoke(response)
//            } else {
//                Log.e("DownloadUtils", "信息错误")
//                onResponseFailure?.invoke(response)
//            }
//        }
//    }
}