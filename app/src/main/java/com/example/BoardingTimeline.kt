package com.example

enum class BoardingPhase(val label: String, val shortLabel: String) {
    WAITING_BUS("Aguardando ônibus", "Aguardando"),
    BUS_AT_GATE("Ônibus no portão", "No portão"),
    BOARDING_OPEN("Embarque aberto", "Embarque"),
    BOARDED("Embarcado", "Embarcado")
}

/** Um momento da jornada no terminal, como o painel do terminal o informaria. */
data class BoardingStage(
    val phase: BoardingPhase,
    val platform: String,
    val gate: String,
    val minutesToDeparture: Int,
    val walkToPlatform: String
)

/**
 * Roteiro simulado dos eventos do terminal. A troca de plataforma no segundo
 * passo mostra como o app reagiria a uma mudança no mundo físico.
 */
val BOARDING_STAGES = listOf(
    BoardingStage(BoardingPhase.WAITING_BUS, platform = "12", gate = "3", minutesToDeparture = 30, walkToPlatform = "~3 min a pé (210 m)"),
    BoardingStage(BoardingPhase.WAITING_BUS, platform = "14", gate = "4", minutesToDeparture = 25, walkToPlatform = "~4 min a pé (260 m)"),
    BoardingStage(BoardingPhase.BUS_AT_GATE, platform = "14", gate = "4", minutesToDeparture = 20, walkToPlatform = "~4 min a pé (260 m)"),
    BoardingStage(BoardingPhase.BOARDING_OPEN, platform = "14", gate = "4", minutesToDeparture = 15, walkToPlatform = "~4 min a pé (260 m)")
)

/** Estado imutável da jornada. Todas as transições retornam uma nova cópia. */
data class BoardingState(
    val stageIndex: Int = 0,
    val isBoarded: Boolean = false
) {
    init {
        require(stageIndex in BOARDING_STAGES.indices) { "stageIndex fora do roteiro: $stageIndex" }
    }

    val stage: BoardingStage get() = BOARDING_STAGES[stageIndex]
    val phase: BoardingPhase get() = if (isBoarded) BoardingPhase.BOARDED else stage.phase
    val canAdvance: Boolean get() = !isBoarded && stageIndex < BOARDING_STAGES.lastIndex

    /** Plataforma original, quando houve troca; null se a plataforma não mudou. */
    val previousPlatform: String?
        get() = BOARDING_STAGES.first().platform.takeIf { it != stage.platform }

    /** Fração da jornada concluída, de 0 a 1, para indicadores de progresso. */
    val progress: Float
        get() = if (isBoarded) 1f else (stageIndex + 1f) / (BOARDING_STAGES.size + 1f)

    fun advance(): BoardingState = if (canAdvance) copy(stageIndex = stageIndex + 1) else this

    /** Na simulação, confirmar o embarque leva a jornada direto ao fim. */
    fun confirmBoarding(): BoardingState =
        copy(stageIndex = BOARDING_STAGES.lastIndex, isBoarded = true)

    fun undoBoarding(): BoardingState = copy(isBoarded = false)
}
