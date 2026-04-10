package com.voxcom.topplethetiki.data.model


data class GameState(

    val roundNumber: Int = 1,
    val maxRounds: Int = 4,

    val tikiStack: List<String> = listOf(),
    val stackVersion: Int = 0,

    val turnOrder: List<String> = listOf(),
    val currentTurnIndex: Int = 0,
    val currentTurn: String = "",

    val turnLocked: Boolean = false,

    val currentPlayerAction: PlayerAction? = null,
    val playerSecrets: Map<String, List<String>> = emptyMap()
)