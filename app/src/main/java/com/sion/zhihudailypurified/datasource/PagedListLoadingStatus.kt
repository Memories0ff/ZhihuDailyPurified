package com.sion.zhihudailypurified.datasource

//列表加载状态
enum class PagedListLoadingStatus {
    INITIAL_LOADING,
    INITIAL_LOADED,
    INITIAL_FAILED,
    AFTER_LOADING,
    AFTER_LOADED,
    AFTER_FAILED,
    COMPLETED
}