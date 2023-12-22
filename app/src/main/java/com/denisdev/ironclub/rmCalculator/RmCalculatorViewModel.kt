package com.denisdev.ironclub.rmCalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import com.denisdev.repository.AuthorRepositoryImpl
import com.denisdev.repository.FormulaRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RmCalculatorViewModel(
    private var uiData: RmUiData = RmUiData(),
    private val rmUseCase: GetRm = GetRmImpl(
        AuthorRepositoryImpl(),
        FormulaRepositoryImpl(),
    ),
): ViewModel() {
    val data = { params: GetRm.Params ->
        rmUseCase(params)
        .map {
            uiData = uiData.copy(
                rm = it.getOrThrow().weight.value,
                author = it.getOrThrow().author
            )
            uiData
        }.catch {
            emit(uiData.copy(RmUiData.DEFAULT_RM))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = uiData,
        )
    }
}

