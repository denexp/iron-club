package com.denisdev.repository

import kotlinx.coroutines.flow.Flow

interface Repository<T, S> {
    fun get(params: T): Flow<S>
}