package com.denisdev.domain.usecases.rmcalculator

import com.denisdev.domain.error.InvalidParameterException
import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.repository.AuthorRepository
import com.denisdev.domain.repository.FormulaRepository
import com.denisdev.domain.usecases.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

interface GetRm: UseCase<GetRm.Params, Flow<Result<RM>>> {
    data class Params(val weight: String,
                      val reps: String,
                      val author: Author,
                      val weightUnit: WeightUnit,
                      val autoFx: Boolean,
    )
}

class GetRmImpl(
    private val authorRepository: AuthorRepository,
    private val formulaRepository: FormulaRepository
): GetRm {
    override operator fun invoke(params: GetRm.Params?): Flow<Result<RM>> {
        return flow {
            params ?: throw InvalidParameterException()
            emit(
                runCatching {
                    val rmParams = RmParams(params.weight, params.reps, params.weightUnit)
                    val author = authorRepository.getAuthor(params.reps.toInt(), params.autoFx, params.author)
                    val formula = formulaRepository.getFormula(rmParams, author)

                    RM(formula.author, formula.calculate())
                }
            )
        }.catch {
            emit(Result.failure(it))
        }
    }
    companion object {
        const val CONSISTENT_RESULT_LIMIT = 14
    }
}