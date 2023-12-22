package com.denisdev.repository

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.repository.FormulaRepository
import com.denisdev.domain.usecases.rmcalculator.RmFormulaBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FormulaRepositoryImpl(private val dispatcher: CoroutineDispatcher = Dispatchers.IO): FormulaRepository {

    override suspend fun getFormula(params: RmParams, author: Author): RmFormula = withContext(dispatcher) {
        RmFormulaBuilder().build(params, author)
    }
}