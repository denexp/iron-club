package com.denisdev.ironclub

import kotlin.math.floor

object Utils {

    fun Float.formatRound(): String {
        return try {
            if (hasNotDecimals(this)) return this.toInt().toString()

            val f = floor((this * 100).toDouble()) / 100

            if(hasNotDecimals(f.toFloat())) return f.toInt().toString()

            String.format("%.2f", f)
        } catch (t: Throwable) { t.printStackTrace(); this.toString() }
    }

    private fun hasNotDecimals(value: Float) = value % 1 == 0f
}