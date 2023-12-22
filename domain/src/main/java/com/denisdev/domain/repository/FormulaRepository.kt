package com.denisdev.domain.repository

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams

interface FormulaRepository {
    suspend fun getFormula(params: RmParams, author: Author): RmFormula
}