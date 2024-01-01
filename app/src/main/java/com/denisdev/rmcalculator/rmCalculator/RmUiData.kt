package com.denisdev.rmcalculator.rmCalculator

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit

data class RmUiData(
    val rm: Float = DEFAULT_RM,
    val author: Author = DEFAULT_AUTHOR,
) {
    companion object {
        const val DEFAULT_RM = 0.0f
        val DEFAULT_AUTHOR = Author.Epley
        val DEFAULT_WEIGHT_UNIT = WeightUnit.Kg
    }
}