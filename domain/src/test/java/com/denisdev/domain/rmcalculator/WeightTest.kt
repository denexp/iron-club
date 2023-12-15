package com.denisdev.domain.rmcalculator

import com.denisdev.domain.error.InvalidParameterException
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class WeightTest {

    @Test
    fun one_kg() {
        val weight = Weight(1F, WeightUnit.Kg)
        val expected = 1F
        assertEquals(expected, weight.value)
    }

    @Test
    fun one_pound() {
        val weight = Weight(1F, WeightUnit.Lb)
        val expected = 1F
        assertEquals(expected, weight.value)
    }

    @Test
    fun one_kg_to_pounds() {
        val weight = Weight(1F, WeightUnit.Kg)
        assertEquals(1F, weight.value)
        val expected = Weight.POUND_UNIT
        assertEquals(expected, weight.toPounds())
    }

    @Test
    fun five_kg_to_pounds() {
        val weight = Weight(5F, WeightUnit.Kg)
        assertEquals(5F, weight.value)
        val expected = Weight.POUND_UNIT * 5F
        assertEquals(expected, weight.toPounds())
    }

    @Test
    fun one_pound_to_kg() {
        val weight = Weight(1F, WeightUnit.Lb)
        assertEquals(1F, weight.value)
        val expected = 1 / Weight.POUND_UNIT
        assertEquals(expected, weight.toKg())
    }

    @Test
    fun five_pound_to_kg() {
        val weight = Weight(5F, WeightUnit.Lb)
        assertEquals(5F, weight.value)
        val expected = 5 / Weight.POUND_UNIT
        assertEquals(expected, weight.toKg())
    }

    @Test
    fun kilogramsLesserThanZero() {
        assertThrows(InvalidParameterException::class.java) {
            Weight(-1F, WeightUnit.Kg)
        }
    }

    @Test
    fun poundsLesserThanZero() {
        assertThrows(InvalidParameterException::class.java) {
            Weight(-50F, WeightUnit.Lb)
        }
    }
}