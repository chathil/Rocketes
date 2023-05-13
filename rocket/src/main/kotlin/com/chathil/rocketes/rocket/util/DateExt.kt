package com.chathil.rocketes.rocket.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toFormattedString(): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(this)
}
