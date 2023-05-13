package com.chathil.rocketes.data.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val API_DATE_FORMAT = "yyyy-MM-dd"

fun String.toDate(): Date {
    return SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault()).parse(this) ?: Date()
}
