package com.denisdev.domain.model.rm.author

import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.rm.formula.RmParams

class OConner(override val params: RmParams): RmFormula {
    override val author = Author.OConner
    override fun calculate() = Weight((params.weight.value * (1 + 0.025 * params.reps.value)).toFloat(), params.weightUnit)
}