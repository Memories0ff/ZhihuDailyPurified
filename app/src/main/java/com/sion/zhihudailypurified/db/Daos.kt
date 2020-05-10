package com.sion.zhihudailypurified.db

import androidx.room.*
import com.sion.zhihudailypurified.entity.StoryBean
import com.sion.zhihudailypurified.entity.StoryContentBean

@Dao
interface StoryDao {
    //------------Story Bean----------------

    @Query("SELECT * FROM StoryBean WHERE date=:date")
    fun obtainStoryBeansByDate(date: String): List<StoryBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStoryBean(storyBean: StoryBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStoryBeans(storyBeans: List<StoryBean>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateStoryBean(storyBean: StoryBean)

}

@Dao
interface StoryContentDao {
    //------------Story Content Bean----------------

    @Query("SELECT * FROM StoryContentBean WHERE id=:id")
    fun obtainStoryContentBeanById(id: Int): List<StoryContentBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStoryContentBean(storyContentBean: StoryContentBean)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateStoryContentBean(storyContentBean: StoryContentBean)
}