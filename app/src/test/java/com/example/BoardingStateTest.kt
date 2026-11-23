package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardingStateTest {

    @Test
    fun `initial state waits for the bus at the original platform`() {
        val state = BoardingState()

        assertEquals(BoardingPhase.WAITING_BUS, state.phase)
        assertEquals("12", state.stage.platform)
        assertNull(state.previousPlatform)
        assertTrue(state.canAdvance)
    }

    @Test
    fun `second stage changes the platform and exposes the previous one`() {
        val state = BoardingState().advance()

        assertEquals("14", state.stage.platform)
        assertEquals("12", state.previousPlatform)
    }

    @Test
    fun `advance stops at boarding open`() {
        var state = BoardingState()
        repeat(BOARDING_STAGES.size + 3) { state = state.advance() }

        assertEquals(BoardingPhase.BOARDING_OPEN, state.phase)
        assertFalse(state.canAdvance)
        assertSame(state, state.advance())
    }

    @Test
    fun `advance returns a new state without changing the original`() {
        val original = BoardingState()

        original.advance()

        assertEquals(0, original.stageIndex)
    }

    @Test
    fun `confirming boarding jumps to the end and completes progress`() {
        val state = BoardingState().confirmBoarding()

        assertEquals(BoardingPhase.BOARDED, state.phase)
        assertEquals(BOARDING_STAGES.lastIndex, state.stageIndex)
        assertEquals(1f, state.progress, 0.0001f)
        assertFalse(state.canAdvance)
    }

    @Test
    fun `undoing boarding returns to boarding open`() {
        val state = BoardingState().confirmBoarding().undoBoarding()

        assertEquals(BoardingPhase.BOARDING_OPEN, state.phase)
    }

    @Test
    fun `progress grows with each stage`() {
        val first = BoardingState()
        val second = first.advance()

        assertTrue(second.progress > first.progress)
        assertTrue(second.progress < 1f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects stage index outside the timeline`() {
        BoardingState(stageIndex = BOARDING_STAGES.size)
    }
}
