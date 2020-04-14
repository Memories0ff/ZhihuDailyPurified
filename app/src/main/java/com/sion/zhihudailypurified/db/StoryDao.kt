package com.sion.zhihudailypurified.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sion.zhihudailypurified.entity.StoryBean

@Dao
interface StoryDao {
    @Query("SELECT * FROM StoryBean WHERE date=:date")
    fun obtainStoryBeansByDate(date: String): DataSource.Factory<String, StoryBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStoryBean(storyBean: StoryBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStoryBeans(storyBeans: List<StoryBean>)
}