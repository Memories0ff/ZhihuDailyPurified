package com.sion.zhihudailypurified.network

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.sion.zhihudailypurified.entity.CommentBean
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter

/**
 * {comments:[{...CommentBean...},{...CommentBean...}]}
 * 类型的json转为List<CommentBean>
 */
class CommentsConverter : Converter<ResponseBody, List<CommentBean>> {

    private val typeToken = object : TypeToken<List<CommentBean>>() {}.type

    override fun convert(value: ResponseBody): List<CommentBean>? {
        val json = value.string()
        if (TextUtils.isEmpty(json)) {
            return null
        }
        return try {
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject["comments"]
            Gson().fromJson<List<CommentBean>>(jsonArray.toString(), typeToken)
        } catch (e: JSONException) {
            null
        } catch (e: JsonSyntaxException) {
            null
        }
    }

}