package com.denisdev.repository

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.usecases.rmcalculator.GetRM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RmRepositoryImpl(private val useCase: GetRM) : RmRepository {
    override fun get(params: GetRM.Params): Flow<Result<RM>> = flow {
        emit(useCase.execute(params))
    }
}