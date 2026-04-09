package com.voxcom.topplethetiki.data.repository

import com.google.firebase.database.ValueEventListener
import com.voxcom.topplethetiki.data.model.GameState
import com.voxcom.topplethetiki.data.model.PlayerAction
import com.voxcom.topplethetiki.data.remote.GameService

class GameRepository(
    private val gameService: GameService = GameService()
) {

    fun initializeGame(roomId: String, gameState: GameState) {
        gameService.initializeGame(roomId, gameState)
    }

    fun observeGameState(roomId: String, listener: (GameState?) -> Unit): ValueEventListener {
        return gameService.observeGameState(roomId, listener)
    }

    fun lockTurn(roomId: String, action: PlayerAction) {
        gameService.lockTurn(roomId, action)
    }

    fun updateTikiStack(roomId: String, newStack: List<String>, newVersion: Int) {
        gameService.updateTikiStack(roomId, newStack, newVersion)
    }

    fun nextTurn(roomId: String, nextIndex: Int, nextPlayer: String) {
        gameService.nextTurn(roomId, nextIndex, nextPlayer)
    }
}