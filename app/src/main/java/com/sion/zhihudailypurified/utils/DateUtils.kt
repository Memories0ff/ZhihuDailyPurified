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

fun currentTime(time: Long): String =
    SimpleDateFormat("MM-dd HH:mm", Locale.CHINESE).format(Date(time * 1000))

fun eightDateDeleteZeroAndYear(dateStr: String): String {
    var m = dateStr.substring(4, 6)
    var d = dateStr.substring(6, 8)
    if (m[0] == '0') {
        m = m.substring(1, 2)
    }
    if (d[0] == '0') {
        d = d.substring(1, 2)
    }
    return "${m} 月 ${d} 日"
}

fun obtainCurrentMonth(): String {
    val cal = Calendar.getInstance()
    return when (cal.get(Calendar.MONTH) + 1) {
        1 -> "一月"
        2 -> "二月"
        3 -> "三月"
        4 -> "四月"
        5 -> "五月"
        6 -> "六月"
        7 -> "七月"
        8 -> "八月"
        9 -> "九月"
        10 -> "十月"
        11 -> "十一月"
        12 -> "十二月"
        else -> "日期错误"
    }
}

fun obtainCurrentDay(): String {
    val cal = Calendar.getInstance()
    return cal.get(Calendar.DAY_OF_MONTH).toString()
}

