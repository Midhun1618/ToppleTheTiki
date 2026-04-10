package com.voxcom.topplethetiki.utils

object GameUtils {

    fun generateRoomCode(): String {
        val chars = ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }

    fun getInitialTikiStack(): List<String> {
        return Constants.INITIAL_TIKI_STACK
    }

    fun isRoundComplete(stack: List<String>): Boolean {
        return stack.size == 3
    }

    fun getNextIndex(current: Int, size: Int): Int {
        return (current + 1) % size
    }
}