package com.denisdev.ironclub.rmCalculator.viewmodel

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.ironclub.rmCalculator.RmCalculatorViewModel
import com.denisdev.ironclub.rmCalculator.RmUiData
import com.denisdev.ironclub.rmCalculator.base.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class RmCalculatorViewModelTest: BaseViewModelTest() {

    private lateinit var sut: RmCalculatorViewModel
    private lateinit var useCase: GetRm
    @Before
    fun setUp() {
        useCase = mockk()
        sut = RmCalculatorViewModel(RmUiData(), useCase)
    }

    @Test
    fun getRmSuccess() = runTest {
        val params = GetRm.Params("100", "8", Author.Brzycki, WeightUnit.Kg, false)
        coEvery { useCase(params) } returns flow { emit(Result.success(RM(Author.Brzycki, Weight(2f, WeightUnit.Kg)))) }

        sut.data(params).produceIn(this).test {
            Assert.assertNotEquals(RmUiData(), it)
            verify { useCase(params) }
        }
    }

    @Test
    fun getRmFailure() = runTest {
        val params = GetRm.Params("100---", "8---", Author.MCGlothin, WeightUnit.Kg, true)
        coEvery { useCase(params) } returns flow { emit(Result.failure(mockk())) }

        sut.data(params).produceIn(this).test {
            Assert.assertEquals(RmUiData(), it)
            verify { useCase(params) }
        }
    }
}