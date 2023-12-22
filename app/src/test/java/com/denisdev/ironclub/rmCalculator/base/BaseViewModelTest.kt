package com.denisdev.ironclub.rmCalculator.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    fun TestScope.launchStateFlow(testDispatcher: TestDispatcher = UnconfinedTestDispatcher(testScheduler), block: suspend CoroutineScope.() -> Unit): Job {
        return backgroundScope.launch(testDispatcher) {
            block(this)
            cancel()
        }
    }
    suspend fun<T> ReceiveChannel<T>.test(block: suspend (T) -> Unit) {
        block(receive())
        cancel()
    }
}