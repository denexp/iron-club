package com.denisdev.domain.model.rm.author

import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Weight
import kotlin.math.E
import kotlin.math.pow

class Mayhew(override val params: RmParams): RmFormula {
    override val author = Author.Mayhew
    override fun calculate() =
        Weight((100 * params.weight.value / (52.2 + 41.9 * E.pow(-0.055 * params.reps.value))).toFloat(), params.weightUnit)
}