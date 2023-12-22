package com.denisdev.ironclub.di

import com.denisdev.domain.repository.AuthorRepository
import com.denisdev.domain.repository.FormulaRepository
import com.denisdev.repository.AuthorRepositoryImpl
import com.denisdev.repository.FormulaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    companion object {
        @Provides
        fun authorRepository(): AuthorRepository = AuthorRepositoryImpl()
        @Provides
        fun formulaRepository(): FormulaRepository = FormulaRepositoryImpl()
    }
}
