package com.sion.zhihudailypurified.sharedPreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

const val SHARED_PREFERENCE_NAME = "zhihu_daily_purified"

private fun getSharedPreference(activity: Activity): SharedPreferences {
    return activity.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
}

fun spPutBoolean(s: String, b: Boolean, activity: Activity) {
    getSharedPreference(activity).edit().apply {
        putBoolean(s, b)
        apply()
    }
}

fun spGetBoolean(s: String, activity: Activity): Boolean {
    return getSharedPreference(activity).getBoolean(s, false)
}