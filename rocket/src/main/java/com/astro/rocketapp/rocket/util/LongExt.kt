package com.astro.rocketapp.rocket.util

import java.text.NumberFormat
import java.util.Locale

fun Long.toMoneyString(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(this)
}
