package com.denisdev.domain.model.rm.author

import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Weight
import kotlin.math.E
import kotlin.math.pow

class Wathen(override val params: RmParams): RmFormula {
    override val author = Author.Wathen
    override fun calculate() =
        Weight((100 * params.weight.value / (48.8 + 53.8 * E.pow(-0.075 * params.reps.value))).toFloat(), params.weightUnit)
}