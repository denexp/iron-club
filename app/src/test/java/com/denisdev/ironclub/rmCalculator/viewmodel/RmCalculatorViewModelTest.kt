package com.denisdev.ironclub.rmCalculator.viewmodel

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRM
import com.denisdev.ironclub.rmCalculator.RmCalculatorViewModel
import com.denisdev.ironclub.rmCalculator.RmUiData
import com.denisdev.ironclub.rmCalculator.base.BaseViewModelTest
import com.denisdev.ironclub.rmCalculator.viewmodel.fakeRepository.FakeRmRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RmCalculatorViewModelTest: BaseViewModelTest() {

    private lateinit var sut: RmCalculatorViewModel
    private lateinit var fakeRepository: FakeRmRepository
    @Before
    fun setUp() {
        fakeRepository = FakeRmRepository()
        sut = RmCalculatorViewModel(rmRepository = fakeRepository)
    }

    @Test
    fun getRmSuccess() = runTest {
        val params = GetRM.Params("100", "8", Author.Brzycki, WeightUnit.Kg, false)
        launchStateFlow {
            fakeRepository.executeUseCase(params)
            sut.data(params).collect()
            Assert.assertNotEquals(RmUiData(), sut.data(params).value)
        }
    }
    @Test
    fun getRmFailure() = runTest {
        val params = GetRM.Params("100---", "8---", Author.MCGlothin, WeightUnit.Kg, true)
        launchStateFlow {
            fakeRepository.executeUseCase(params)
            sut.data(params).collect()
            Assert.assertEquals(RmUiData(), sut.data(params).value)
        }
    }
}