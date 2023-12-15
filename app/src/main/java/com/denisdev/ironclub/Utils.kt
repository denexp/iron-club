package com.denisdev.ironclub

import java.math.RoundingMode
import java.text.DecimalFormat

object Utils {

    fun Float.round(pattern: String, roundingMode: RoundingMode = RoundingMode.FLOOR): String {
        val df = DecimalFormat(pattern)
        df.roundingMode = roundingMode
        return try { df.format(this) } catch (t: Throwable) { t.stackTrace; this.toString() }
    }
}