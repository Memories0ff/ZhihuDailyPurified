package com.sion.zhihudailypurified.network

import com.sion.zhihudailypurified.entity.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiServices {
    @GET
    fun obtainPic(@Url url: String): Call<ResponseBody>

    @Headers(
        "Host:news-at.zhihu.com",
        "User-Agent:DailyApi/4 (Linux; Android 5.1.1; OPPO R11 Build/OPPO /R11/R11/NMF26X/zh_CN) Google-HTTP-Java-Client/1.22.0 (gzip) Google-HTTP-Java-Client/1.22.0 (gzip)",
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
        "Connection:keep-alive",
        "Pragma:no-cache",
        "Cache-Control:no-cache"
    )
    @GET("stories/latest")
    fun obtainLatest(): Call<LatestStories>

    @Headers(
        "Host:news-at.zhihu.com",
        "User-Agent:DailyApi/4 (Linux; Android 5.1.1; OPPO R11 Build/OPPO /R11/R11/NMF26X/zh_CN) Google-HTTP-Java-Client/1.22.0 (gzip) Google-HTTP-Java-Client/1.22.0 (gzip)",
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
        "Connection:keep-alive",
        "Pragma:no-cache",
        "Cache-Control:no-cache"
    )
    @GET("stories/before/{date}")
    fun obtainPastNews(@Path("date") date: String): Call<PastStroies>

    @Headers(
        "Host:news-at.zhihu.com",
        "User-Agent:DailyApi/4 (Linux; Android 5.1.1; OPPO R11 Build/OPPO /R11/R11/NMF26X/zh_CN) Google-HTTP-Java-Client/1.22.0 (gzip) Google-HTTP-Java-Client/1.22.0 (gzip)",
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
        "Connection:keep-alive",
        "Pragma:no-cache",
        "Cache-Control:no-cache"
    )
    @GET("story/{id}")
    fun obtainContent(@Path("id") storyId: String): Call<StoryContentBean>

    @Headers(
        "Host:news-at.zhihu.com",
        "User-Agent:DailyApi/4 (Linux; Android 5.1.1; OPPO R11 Build/OPPO /R11/R11/NMF26X/zh_CN) Google-HTTP-Java-Client/1.22.0 (gzip) Google-HTTP-Java-Client/1.22.0 (gzip)",
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
        "Connection:keep-alive",
        "Pragma:no-cache",
        "Cache-Control:no-cache"
    )
    @GET("story-extra/{id}")
    fun obtainContentExtra(@Path("id") storyId: String): Call<StoryContentExtraBean>

    @Headers(
        "Host:news-at.zhihu.com",
        "User-Agent:DailyApi/4 (Linux; Android 5.1.1; OPPO R11 Build/OPPO /R11/R11/NMF26X/zh_CN) Google-HTTP-Java-Client/1.22.0 (gzip) Google-HTTP-Java-Client/1.22.0 (gzip)",
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
        "Connection:keep-alive",
        "Pragma:no-cache",
        "Cache-Control:no-cache"
    )
    @GET("story/{newsId}/long-comments")
    fun obtainLongComments(@Path("newsId") newsId: String): Call<List<CommentBean>>

    @Headers(
        "Host:news-at.zhihu.com",
        "User-Agent:DailyApi/4 (Linux; Android 5.1.1; OPPO R11 Build/OPPO /R11/R11/NMF26X/zh_CN) Google-HTTP-Java-Client/1.22.0 (gzip) Google-HTTP-Java-Client/1.22.0 (gzip)",
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
        "Connection:keep-alive",
        "Pragma:no-cache",
        "Cache-Control:no-cache"
    )
    @GET("story/{newsId}/short-comments")
    fun obtainShortComments(@Path("newsId") newsId: String): Call<List<CommentBean>>

    @Headers(
        "Host:news-at.zhihu.com",
        "User-Agent:DailyApi/4 (Linux; Android 5.1.1; OPPO R11 Build/OPPO /R11/R11/NMF26X/zh_CN) Google-HTTP-Java-Client/1.22.0 (gzip) Google-HTTP-Java-Client/1.22.0 (gzip)",
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
        "Connection:keep-alive",
        "Pragma:no-cache",
        "Cache-Control:no-cache"
    )
    @GET("story/{newsId}/short-comments/before/{commentId}")
    fun obtainShortComments(
        @Path("newsId") newsId: String,
        @Path("commentId") commentId: String
    ): Call<List<CommentBean>>
}