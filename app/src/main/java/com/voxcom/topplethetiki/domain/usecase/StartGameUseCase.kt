package com.voxcom.topplethetiki.domain.usecase

import com.voxcom.topplethetiki.data.model.GameState
import com.voxcom.topplethetiki.data.repository.GameRepository

class StartGameUseCase(
    private val repository: GameRepository = GameRepository()
) {

    fun execute(roomId: String, players: List<String>) {

        val initialStack = listOf(
            "t1","t2","t3","t4","t5","t6","t7","t8","t9"
        )

        val gameState = GameState(
            tikiStack = initialStack,
            turnOrder = players,
            currentTurnIndex = 0,
            currentTurn = players.first(),
            maxRounds = players.size
        )

        repository.initializeGame(roomId, gameState)
    }
}