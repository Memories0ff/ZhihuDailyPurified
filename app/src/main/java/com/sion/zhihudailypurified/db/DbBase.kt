package com.sion.zhihudailypurified.db

val dbStoryListServices by lazy {
    AppDatabase.instance.storyDao()
}

val dbContentServices by lazy {
    AppDatabase.instance.storyContentDao()
}