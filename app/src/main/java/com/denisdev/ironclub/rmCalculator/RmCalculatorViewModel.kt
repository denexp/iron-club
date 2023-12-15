package com.denisdev.ironclub.rmCalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisdev.domain.usecases.rmcalculator.GetRM
import com.denisdev.ironclub.base.UiState
import kotlinx.coroutines.launch

class RmCalculatorViewModel(
    val uiState: UiState<RmUiData> = UiState(RmUiData()),
    private val rmUseCase: GetRM = GetRM(),
): ViewModel() {
    fun getRm(params: GetRM.Params) {
        viewModelScope.launch {
            val result = rmUseCase.execute(params)
            if (result.isFailure) return@launch
            uiState.update { uiData ->
                uiData.copy(
                    rm = result.getOrThrow().weight.value,
                    author = result.getOrThrow().author
                )
            }
        }
    }
}