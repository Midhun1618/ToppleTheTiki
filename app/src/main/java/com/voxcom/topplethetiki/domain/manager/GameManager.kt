package com.voxcom.topplethetiki.domain.manager

import com.voxcom.topplethetiki.data.model.GameState
import com.voxcom.topplethetiki.data.model.PlayerAction
import com.voxcom.topplethetiki.data.repository.GameRepository

class GameManager(
    private val repository: GameRepository = GameRepository(),
    private val stackManager: StackManager = StackManager(),
    private val turnManager: TurnManager = TurnManager()
) {

    fun playTurn(
        roomId: String,
        gameState: GameState,
        playerId: String,
        action: String,
        tiki: String
    ) {

        if (!turnManager.isPlayerTurn(gameState.currentTurn, playerId)) return

        if (gameState.turnLocked) return

        val playerAction = PlayerAction(
            playerId = playerId,
            action = action,
            tiki = tiki
        )

        repository.lockTurn(roomId, playerAction)

        val newStack = stackManager.applyAction(
            gameState.tikiStack,
            action,
            tiki
        )

        val newVersion = gameState.stackVersion + 1

        repository.updateTikiStack(roomId, newStack, newVersion)

        if (stackManager.isRoundComplete(newStack)) {
            return
        }

        val nextIndex = turnManager.getNextTurnIndex(
            gameState.currentTurnIndex,
            gameState.turnOrder.size
        )

        val nextPlayer = turnManager.getNextPlayer(
            gameState.turnOrder,
            nextIndex
        )

        repository.nextTurn(roomId, nextIndex, nextPlayer)
    }
}