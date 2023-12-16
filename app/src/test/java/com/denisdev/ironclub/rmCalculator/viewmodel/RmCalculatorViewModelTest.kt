package com.denisdev.ironclub.rmCalculator.viewmodel

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRM
import com.denisdev.ironclub.base.UiState
import com.denisdev.ironclub.rmCalculator.RmCalculatorViewModel
import com.denisdev.ironclub.rmCalculator.RmUiData
import com.denisdev.ironclub.rmCalculator.base.BaseViewModelTest
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RmCalculatorViewModelTest: BaseViewModelTest() {

    private lateinit var sut: RmCalculatorViewModel
    private lateinit var uiState: UiState<RmUiData>
    @Before
    fun setUp() {
        uiState = spyk(UiState(RmUiData()))
        sut = RmCalculatorViewModel(uiState)
    }

    @Test
    fun getUiData() {
    }

    @Test
    fun getRmSuccess() {
        val params = GetRM.Params("100", "8", Author.Brzycki, WeightUnit.Kg, false)

        sut.getRm(params)

        verify { uiState.update(any()) }
    }
    @Test
    fun getRmFailure() {
        val params = GetRM.Params("100---", "8---", Author.MCGlothin, WeightUnit.Kg, true)

        sut.getRm(params)

        verify { uiState.defaultState() }
    }
}