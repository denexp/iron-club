package com.denisdev.domain.formula

import com.denisdev.domain.model.rm.author.*
import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Reps
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.RmFormulaBuilder
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import kotlin.math.round

@RunWith(Parameterized::class)
class RmFormulaTest(private val index: Int, private val params: RmParams, private val author: Author) {

    companion object {
        @JvmStatic
        @Parameters
        fun params(): Array<Array<Any>> {
            return arrayOf(
                arrayOf(0, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.Epley),
                arrayOf(1, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.OConner),
                arrayOf(2, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.Lombardi),
                arrayOf(3, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.Brzycki),
                arrayOf(4, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.Mayhew),
                arrayOf(5, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.Wathen),
                arrayOf(6, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.Lander),
                arrayOf(7, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.MCGlothin),
                arrayOf(8, RmParams(Weight(100F, WeightUnit.Kg), Reps(8)), Author.EpleyHighReps)
            )
        }
    }

    @Test
    fun getAuthor() {
        val list = arrayOf(
            Author.Epley,
            Author.OConner,
            Author.Lombardi,
            Author.Brzycki,
            Author.Mayhew,
            Author.Wathen,
            Author.Lander,
            Author.MCGlothin,
            Author.EpleyHighReps
        )
        val formula = RmFormulaBuilder().build(params, author)
        Assert.assertEquals(list[index], formula.author)
    }

    @Test
    fun calculateRM() {
        val list = arrayOf(
            126F,
            120F,
            123F,
            124F,
            126F,
            128F,
            125F,
            124F,
            124F
        )
        val formula = RmFormulaBuilder().build(params, author)
        Assert.assertEquals(list[index], round(formula.calculate().value))
    }

    @Test
    fun calculateRMWithWeightZero() {
        val formula = RmFormulaBuilder().build(params, author)
        val rmFormula = formula.apply { params.weight = Weight(0F, WeightUnit.Kg) }
        Assert.assertEquals(0F, rmFormula.calculate().value)
    }
}