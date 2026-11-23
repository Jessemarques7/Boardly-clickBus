package com.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoardingViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val stepDelay = 1_000L

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun TestScope.passSteps(steps: Int) {
        advanceTimeBy(stepDelay * steps)
        runCurrent()
    }

    @Test
    fun `auto advances one stage per interval`() = runTest(dispatcher) {
        val viewModel = BoardingViewModel(stepDelay)

        passSteps(1)

        assertEquals(1, viewModel.state.value.stageIndex)
    }

    @Test
    fun `auto advance stops at boarding open`() = runTest(dispatcher) {
        val viewModel = BoardingViewModel(stepDelay)

        advanceUntilIdle()

        assertEquals(BoardingPhase.BOARDING_OPEN, viewModel.state.value.phase)
    }

    @Test
    fun `manual advance moves one stage immediately`() = runTest(dispatcher) {
        val viewModel = BoardingViewModel(stepDelay)

        viewModel.advance()

        assertEquals(1, viewModel.state.value.stageIndex)
    }

    @Test
    fun `confirming stops the timer and marks as boarded`() = runTest(dispatcher) {
        val viewModel = BoardingViewModel(stepDelay)

        viewModel.confirmBoarding()
        advanceUntilIdle()

        assertEquals(BoardingPhase.BOARDED, viewModel.state.value.phase)
    }

    @Test
    fun `repeat validation reopens boarding`() = runTest(dispatcher) {
        val viewModel = BoardingViewModel(stepDelay)
        viewModel.confirmBoarding()

        viewModel.repeatValidation()

        assertEquals(BoardingPhase.BOARDING_OPEN, viewModel.state.value.phase)
    }

    @Test
    fun `restart goes back to the first stage and resumes the timer`() = runTest(dispatcher) {
        val viewModel = BoardingViewModel(stepDelay)
        viewModel.confirmBoarding()

        viewModel.restart()
        assertEquals(BoardingState(), viewModel.state.value)

        passSteps(1)
        assertEquals(1, viewModel.state.value.stageIndex)
    }
}
