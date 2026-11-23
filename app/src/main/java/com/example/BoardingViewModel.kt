package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Intervalo entre eventos simulados do terminal. */
const val SIMULATION_STEP_DELAY_MS = 10_000L

/**
 * Simula as atualizações que viriam do terminal: a cada intervalo a jornada
 * avança um passo, até o embarque abrir. Também pode ser avançada à mão,
 * para controlar o ritmo durante a apresentação.
 */
class BoardingViewModel(
    private val stepDelayMillis: Long = SIMULATION_STEP_DELAY_MS
) : ViewModel() {

    private val _state = MutableStateFlow(BoardingState())
    val state: StateFlow<BoardingState> = _state.asStateFlow()

    private var autoAdvanceJob: Job? = null

    init {
        startAutoAdvance()
    }

    fun advance() {
        _state.update { it.advance() }
    }

    fun confirmBoarding() {
        autoAdvanceJob?.cancel()
        _state.update { it.confirmBoarding() }
    }

    fun repeatValidation() {
        _state.update { it.undoBoarding() }
    }

    fun restart() {
        _state.value = BoardingState()
        startAutoAdvance()
    }

    private fun startAutoAdvance() {
        autoAdvanceJob?.cancel()
        autoAdvanceJob = viewModelScope.launch {
            while (_state.value.canAdvance) {
                delay(stepDelayMillis)
                _state.update { it.advance() }
            }
        }
    }
}
