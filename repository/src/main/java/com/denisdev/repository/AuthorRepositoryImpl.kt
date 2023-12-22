package com.denisdev.repository

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.repository.AuthorRepository
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorRepositoryImpl(private val dispatcher: CoroutineDispatcher = Dispatchers.IO): AuthorRepository {
    override suspend fun getAuthor(reps: Int, autoFx: Boolean, default: Author): Author = withContext(dispatcher) {
        if (!autoFx)
            return@withContext default

        if ((0..10).contains(reps))
            return@withContext Author.Brzycki

        if ((11..GetRmImpl.CONSISTENT_RESULT_LIMIT).contains(reps))
            return@withContext Author.Epley

        return@withContext Author.EpleyHighReps
    }
}