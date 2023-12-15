package com.denisdev.domain.model.rm.formula

import com.denisdev.domain.model.Formula
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.usecases.rmcalculator.GetRM

interface RmFormula: Formula<Author, Weight> {
    val params: RmParams
    override val author: Author
    override fun calculate(): Weight
}