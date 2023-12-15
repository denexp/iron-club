package com.denisdev.domain.rmcalculator


import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRM
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetRMsTest {

    @Test
    fun getRmNotAutoFx() {
        val useCase = GetRM()
        val params = GetRM.Params("100", "8", Author.Mayhew, WeightUnit.Kg, false)

        runTest {
            val result = useCase.execute(params)

            Assert.assertTrue(result.isSuccess)
            Assert.assertEquals(Author.Mayhew, result.getOrThrow().author)
        }
    }

    @Test
    fun getRmAutoFx() {
        val useCase = GetRM()
        val params = GetRM.Params("100", "8", Author.Mayhew, WeightUnit.Kg, true)

        runTest {
            val result = useCase.execute(params)

            Assert.assertTrue(result.isSuccess)
            Assert.assertEquals(Author.Brzycki, result.getOrThrow().author)
        }
    }

    @Test
    fun getRmFailureByWrongParameters() {
        val useCase = GetRM()
        val params = GetRM.Params("TEST", "TEST", Author.Mayhew, WeightUnit.Kg, true)

        runTest {
            val result = useCase.execute(params)
            Assert.assertTrue(result.isFailure)
        }
    }
}