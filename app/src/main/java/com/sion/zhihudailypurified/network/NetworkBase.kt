package com.sion.zhihudailypurified.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val appApiBaseUrl = "https://news-at.zhihu.com/api/4/"
//private const val appApiBaseUrl = "https://www.zhihu.com/api/4/"

val apiServices by lazy {
    val r = Retrofit.Builder()
        .addConverterFactory(ResponseBodyConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(appApiBaseUrl)
        .build()
        .create(ApiServices::class.java)
    r
}