package com.voxcom.topplethetiki.domain.manager

class TurnManager {

    fun getNextTurnIndex(currentIndex: Int, totalPlayers: Int): Int {
        return (currentIndex + 1) % totalPlayers
    }

    fun getNextPlayer(turnOrder: List<String>, nextIndex: Int): String {
        return turnOrder[nextIndex]
    }

    fun isPlayerTurn(currentTurn: String, playerId: String): Boolean {
        return currentTurn == playerId
    }
}