package com.denisdev.domain.model.rm.author

import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Weight

class MCGlothin(override val params: RmParams): RmFormula {
    override val author = Author.MCGlothin
    override fun calculate() =
        Weight((params.weight.value / (1.0278 - 0.0278 * params.reps.value)).toFloat(), params.weightUnit)
}