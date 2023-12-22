package com.denisdev.ironclub

import com.denisdev.ironclub.Utils.formatRound
import org.junit.Assert
import org.junit.Test

class FormatRoundTest {

    @Test
    fun roundRemoveDecimals() {
        Assert.assertEquals("180", 180.00847f.formatRound())
    }
    @Test
    fun roundNoDecimals() {
        Assert.assertEquals("1", 1f.formatRound())
    }
    @Test
    fun simpleRound() {
        Assert.assertEquals("0.90", 0.9000426f.formatRound())
    }
    @Test
    fun roundFloor() {
        Assert.assertEquals("0.91", 0.9168426f.formatRound())
    }
    @Test
    fun addZero() {
        Assert.assertEquals("0.90", 0.9f.formatRound())
    }
    @Test
    fun addZeros() {
        Assert.assertEquals("0", 0.0f.formatRound())
    }
}