package com.denisdev.repository

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.usecases.rmcalculator.GetRM
import kotlinx.coroutines.flow.Flow

interface RmRepository: Repository<GetRM.Params, Result<RM>> {
    override fun get(params: GetRM.Params): Flow<Result<RM>>
}