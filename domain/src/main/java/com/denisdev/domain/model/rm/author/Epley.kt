package com.denisdev.domain.model.rm.author

import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams

class Epley(override val params: RmParams): RmFormula {
    override val author = Author.Epley
    override fun calculate() = Weight((params.weight.value * ( 1 + (0.033 * params.reps.value))).toFloat(), params.weightUnit)
}