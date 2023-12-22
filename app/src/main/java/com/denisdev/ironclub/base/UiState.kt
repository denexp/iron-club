package com.denisdev.ironclub.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UiState<T>(private val provider: T) {
    private val _data: MutableStateFlow<T> = MutableStateFlow(provider)
    val data: StateFlow<T> = _data
    fun update(value: (T) -> T) = _data.update { value(it) }

    fun defaultState() = _data.update { provider }
}