package com.sion.zhihudailypurified.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException

fun <T> Call<T>.callIO(
    onFailure: ((t: Throwable) -> Unit)? = null,
    onResponseFailure: ((response: Response<T>) -> Unit)? = null,
    onResponseSuccess: ((response: Response<T>) -> Unit)? = null
) {
    GlobalScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.IO) {
            try {
                execute()
            } catch (e: IOException) {
                Log.e("DownloadUtils", "IOException")
                onFailure?.invoke(e)
                null
            } catch (e: RuntimeException) {
                Log.e("DownloadUtils", "RuntimeException")
                onFailure?.invoke(e)
                null
            } catch (e: Exception) {
                Log.e("DownloadUtils", "Exception")
                onFailure?.invoke(e)
                null
            }
        }?.let { response ->
            if (response.isSuccessful) {
                onResponseSuccess?.invoke(response)
            } else {
                onResponseFailure?.invoke(response)
            }
        }
    }
}