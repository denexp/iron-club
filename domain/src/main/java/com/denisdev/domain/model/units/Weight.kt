package com.denisdev.domain.model.units

import com.denisdev.domain.error.InvalidParameterException


data class Weight(var value: Float = 0F, val weightUnit: WeightUnit) {
    companion object {
        const val POUND_UNIT = 2.2046225F
    }

    init {
        if (value < 0F)
            throw InvalidParameterException("weight: $value , unit: $weightUnit")
    }

    fun toPounds() = value * POUND_UNIT
    fun toKg() = value / POUND_UNIT
}