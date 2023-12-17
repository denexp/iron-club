package com.denisdev.ironclub.rmCalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisdev.domain.usecases.rmcalculator.GetRM
import com.denisdev.repository.RmRepository
import com.denisdev.repository.RmRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RmCalculatorViewModel(
    private var uiData: RmUiData = RmUiData(),
    private val rmRepository: RmRepository = RmRepositoryImpl(
        GetRM()
    ),
): ViewModel() {
    val data = { params: GetRM.Params ->
        rmRepository.get(params)
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = uiData,
        )
    }
}

