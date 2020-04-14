package com.sion.zhihudailypurified.db

import androidx.room.*
import com.sion.zhihudailypurified.App
import com.sion.zhihudailypurified.entity.StoryBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [StoryBean::class], version = 1)
abstract class AppDatabase private constructor() : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object {
        val instance: AppDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            var temp: AppDatabase? = null
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    temp = Room.databaseBuilder(
                        App.getAppContext(),
                        AppDatabase::class.java,
                        "zhihu_daily_purified.db"
                    ).build()
                }
            }
            temp!!
        }
    }
}