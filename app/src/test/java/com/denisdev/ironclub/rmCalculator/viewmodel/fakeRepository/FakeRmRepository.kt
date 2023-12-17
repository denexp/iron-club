package com.denisdev.ironclub.rmCalculator.viewmodel.fakeRepository

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.usecases.rmcalculator.GetRM
import com.denisdev.repository.RmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeRmRepository(val useCase: GetRM = GetRM()) : com.denisdev.repository.RmRepository {
    private val flow = MutableSharedFlow<Result<RM>>()
    suspend fun emit(value: Result<RM>) = flow.emit(value)
    suspend fun executeUseCase(params: GetRM.Params) = emit(useCase.execute(params))
    override fun get(params: GetRM.Params): Flow<Result<RM>> = flow
}