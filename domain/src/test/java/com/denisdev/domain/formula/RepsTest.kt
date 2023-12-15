package com.denisdev.domain.formula

import com.denisdev.domain.model.units.Reps
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.security.InvalidParameterException

class RepsTest {

    @Test
    fun defaultConstructor() {
        assertEquals(0, Reps().value)
    }

    @Test
    fun five_reps() {
        assertEquals(5, Reps(5).value)
    }

    @Test
    fun negativeValueInvalidParameterException() {
        Assert.assertThrows(InvalidParameterException::class.java) {
            Reps(-5)
        }
    }
}