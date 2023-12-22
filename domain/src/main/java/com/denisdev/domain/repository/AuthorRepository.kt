package com.denisdev.domain.repository

import com.denisdev.domain.model.rm.author.Author

interface AuthorRepository {
    suspend fun getAuthor(reps: Int, autoFx: Boolean, default: Author): Author
}