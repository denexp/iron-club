package com.denisdev.domain.model.units

import java.security.InvalidParameterException

data class Reps(
    val value: Int = 0
) {
    init {
        if (value < 0)
            throw InvalidParameterException()
    }
}