package com.sion.zhihudailypurified

import android.app.Application
import android.content.Context
import android.content.res.Resources

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        appResources = resources
    }

    companion object {
        private var appContext: Context? = null
        private var appResources: Resources? = null

        fun getAppContext(): Context {
            return appContext!!
        }

        fun getAppResources(): Resources {
            return appResources!!
        }

    }
}