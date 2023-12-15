package com.denisdev.domain.model.rm.author

import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Weight

class Lander(override val params: RmParams): RmFormula {
    override val author = Author.Lander
    override fun calculate() =
        Weight((100 * params.weight.value / (101.3F - (2.67123 * params.reps.value))).toFloat(), params.weightUnit)
}