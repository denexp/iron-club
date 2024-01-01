package com.denisdev.ironclub.di

import com.denisdev.ironclub.rmCalculator.RmUiData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        fun rmUiData() = RmUiData()
    }
}