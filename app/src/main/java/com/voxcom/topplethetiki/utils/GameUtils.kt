package com.voxcom.topplethetiki.utils

object GameUtils {

    private val chars = ('A'..'Z') + ('0'..'9')

    fun generateRoomCode(length: Int = 6): String {
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    fun getInitialTikiStack(): List<String> {
        return Constants.INITIAL_TIKI_STACK.toList() // 🔥 avoid mutation
    }

    fun isRoundComplete(stack: List<String>): Boolean {
        return stack.size <= 3 // 🔥 safer condition
    }

    fun getNextIndex(current: Int, size: Int): Int {
        return if (size == 0) 0 else (current + 1) % size
    }
}