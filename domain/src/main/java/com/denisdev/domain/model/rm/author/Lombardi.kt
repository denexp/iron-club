package com.denisdev.domain.model.rm.author

import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Weight
import kotlin.math.pow

class Lombardi(override val params: RmParams): RmFormula {
    override val author = Author.Lombardi
    override fun calculate() =
        Weight((params.weight.value * params.reps.value.toDouble().pow(0.10)).toFloat(), params.weightUnit)
}