package com.voxcom.topplethetiki.data.model


data class Player(
    val uid: String = "",
    val username: String = "",
    val avatar: String = "",

    val isReady: Boolean = false,

    val score: Int = 0,
    val position: Int = 0,

    val secretCard: List<String> = listOf(),

    val cards: Map<String, Boolean> = mapOf(
        "up1" to true,
        "up2" to true,
        "up3" to true,
        "toss" to true,
        "top" to true,
        "bottom" to true,
        "up1_copy" to true
    )
)