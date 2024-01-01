package com.denisdev.rmcalculator.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    suspend fun<T> ReceiveChannel<T>.test(block: suspend (T) -> Unit) {
        block(receive())
        cancel()
    }
}