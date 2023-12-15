package com.denisdev.domain.usecases.rmcalculator

import com.denisdev.domain.model.rm.author.*
import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams

class RmFormulaBuilder() {
    fun build(rmParams: RmParams, author: Author): RmFormula {
        return when(author) {
            Author.OConner -> OConner(rmParams)
            Author.Epley -> Epley(rmParams)
            Author.Lombardi -> Lombardi(rmParams)
            Author.Brzycki -> Brzycki(rmParams)
            Author.Mayhew -> Mayhew(rmParams)
            Author.Wathen -> Wathen(rmParams)
            Author.Lander -> Lander(rmParams)
            Author.MCGlothin -> MCGlothin(rmParams)
            Author.EpleyHighReps -> EpleyHighReps(rmParams)
        }
    }
}