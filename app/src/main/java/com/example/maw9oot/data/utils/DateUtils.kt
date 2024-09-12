package com.example.maw9oot.data.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getMonthAbbreviation(month: java.time.Month): String {
    return when (month) {
        java.time.Month.JANUARY -> "Jan"
        java.time.Month.FEBRUARY -> "Feb"
        java.time.Month.MARCH -> "Mar"
        java.time.Month.APRIL -> "Apr"
        java.time.Month.MAY -> "May"
        java.time.Month.JUNE -> "Jun"
        java.time.Month.JULY -> "Jul"
        java.time.Month.AUGUST -> "Aug"
        java.time.Month.SEPTEMBER -> "Sep"
        java.time.Month.OCTOBER -> "Oct"
        java.time.Month.NOVEMBER -> "Nov"
        java.time.Month.DECEMBER -> "Dec"
    }
}

fun getMonthYear(date: String): String {
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(date, dateFormat)
    val month = localDate.month
    val year = localDate.year
    return "${getMonthAbbreviation(month)}/${year % 100}"
}
