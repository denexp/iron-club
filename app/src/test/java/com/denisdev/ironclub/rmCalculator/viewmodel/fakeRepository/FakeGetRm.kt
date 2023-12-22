package com.denisdev.ironclub.rmCalculator.viewmodel.fakeRepository

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeGetRm : GetRm {
    private val flow = MutableSharedFlow<Result<RM>>()
    suspend fun emit(value: Result<RM>) = flow.emit(value)
    override fun invoke(params: GetRm.Params?): Flow<Result<RM>> = flow
}