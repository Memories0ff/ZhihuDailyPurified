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
            return this.appContext
                ?: throw Exception("Application还未被系统创建")
        }

        fun getApplication(): App {
            val appContext = appContext ?: throw Exception("Application还未被系统创建")
            return appContext as App
        }

        fun getAppResources(): Resources {
            return this.appResources
                ?: throw Exception("Application还未被系统创建")
        }
    }
}