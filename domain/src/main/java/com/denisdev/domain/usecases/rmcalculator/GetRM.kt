package com.denisdev.domain.usecases.rmcalculator

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.UseCaseWith

class GetRM: UseCaseWith<GetRM.Params, RM>() {
    data class Params(val weight: String,
                      val reps: String,
                      val author: Author,
                      val weightUnit: WeightUnit,
                      val autoFx: Boolean,
    )

    override suspend fun execute(params: Params): Result<RM> {
        return runCatching {
            var author = params.author
            if (params.autoFx) {
                if ((0..10).contains(params.reps.toInt()))
                    author = Author.Brzycki
                if ((11..CONSISTENT_RESULT_LIMIT).contains(params.reps.toInt()))
                    author = Author.Epley
                if (params.reps.toInt() > CONSISTENT_RESULT_LIMIT)
                    author = Author.EpleyHighReps
            }

            val formula = RmFormulaBuilder().build(
                RmParams(params.weight, params.reps, params.weightUnit),
                author
            )

            RM(formula.author, formula.calculate())
        }
    }
    companion object {
        const val CONSISTENT_RESULT_LIMIT = 14
    }
}