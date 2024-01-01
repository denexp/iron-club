package com.denisdev.domain.di

import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun getRm(useCase: GetRmImpl): GetRm
}