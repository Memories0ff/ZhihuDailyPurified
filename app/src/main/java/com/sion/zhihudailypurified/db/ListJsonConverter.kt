package com.sion.zhihudailypurified.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListJsonConverter {

    private val typeToken = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun jsonToList(json: String): List<String> {
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun listToJson(list: List<String>): String {
        return Gson().toJson(list)
    }
}