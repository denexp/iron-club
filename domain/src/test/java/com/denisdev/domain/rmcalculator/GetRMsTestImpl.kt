package com.denisdev.domain.rmcalculator


import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetRMsTestImpl {

    lateinit var useCase: GetRm
    @Before
    fun setUp() {
        useCase = GetRmImpl(
            mockk {
                coEvery { getAuthor(any(), any(), any()) } returns Author.Brzycki
            },
            mockk {
                coEvery { getFormula(any(), any()) } returns mockk {
                    every { author } returns Author.Brzycki
                    every { calculate() } returns mockk()
                }
            }
        )
    }
    @Test
    fun success() {
        val params = GetRm.Params("100", "8", Author.Mayhew, WeightUnit.Kg, true)

        runTest {
            useCase(params).map { result ->
                Assert.assertTrue(result.isSuccess)
            }.collect()
        }
    }

    @Test
    fun failureWrongParameters() {
        val params = GetRm.Params("TEST", "TEST", Author.Mayhew, WeightUnit.Kg, true)

        runTest {
            useCase(params).map { result ->
                Assert.assertTrue(result.isFailure)
            }.collect()
        }
    }
    @Test
    fun failureNoParameters() {
        runTest {
            useCase().map { result ->
                Assert.assertTrue(result.isFailure)
            }.collect()
        }
    }
}