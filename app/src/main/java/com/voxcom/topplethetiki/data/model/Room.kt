package com.voxcom.topplethetiki.data.model


data class Room(
    val roomId: String = "",
    val hostId: String = "",

    val status: String = "waiting", // waiting | playing | finished
    val maxPlayers: Int = 4,

    val players: Map<String, Player> = mapOf(),

    val avatarsSelected: List<String> = listOf()
)