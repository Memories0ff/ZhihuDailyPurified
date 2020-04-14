package com.sion.zhihudailypurified.utils

import java.text.SimpleDateFormat
import java.util.*

fun today(): String {
    val today = Date()
    val formatter = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
    return formatter.format(today)
}

fun obtainDay(dateStr: String, offset: Int): String {
    val formatter = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
    val date = formatter.parse(dateStr)
    val calendar = GregorianCalendar()
    calendar.time = date!!
    calendar.add(Calendar.DATE, offset)
    return formatter.format(calendar.time)
}

fun obtainDay(date: Date, offset: Int): String {
    val formatter = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
    val calendar = GregorianCalendar()
    calendar.time = date
    calendar.add(Calendar.DATE, offset)
    return formatter.format(calendar.time)
}