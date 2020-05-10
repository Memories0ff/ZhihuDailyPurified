package com.sion.zhihudailypurified.db

import androidx.room.*
import com.sion.zhihudailypurified.App
import com.sion.zhihudailypurified.entity.StoryBean
import com.sion.zhihudailypurified.entity.StoryContentBean

@Database(entities = [StoryBean::class, StoryContentBean::class], version = 1, exportSchema = false)
abstract class AppDatabase /*private constructor()*/ : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun storyContentDao(): StoryContentDao

    companion object {
        val instance: AppDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(
                App.getAppContext(),
                AppDatabase::class.java,
                "zhihu_daily_purified.db"
            ).build()
        }
    }
}