package com.sion.zhihudailypurified.network

import com.sion.zhihudailypurified.entity.CommentBean
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResponseBodyConverterFactory : Converter.Factory() {

    companion object {
        fun create() = ResponseBodyConverterFactory()
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? =
        //用于List<CommentBean>
        if (type is ParameterizedType
            && type.rawType == List::class.java
            && type.actualTypeArguments.let {
                it.size == 1 && it[0] == CommentBean::class.java
            }
        ) {
            CommentsConverter()
        } else {
            null
        }
}