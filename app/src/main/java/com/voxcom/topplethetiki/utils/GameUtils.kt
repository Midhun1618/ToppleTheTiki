package com.voxcom.topplethetiki.utils

object GameUtils {

    private val chars = ('a'..'b') + ('1'..'9')

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
    fun getSecretCards(): MutableList<List<String>> {
        return mutableListOf(
            listOf("t1","t2","t3"),
            listOf("t1","t2","t4"),
            listOf("t1","t3","t5"),
            listOf("t2","t3","t6"),
            listOf("t4","t5","t6"),
            listOf("t7","t8","t9"),
            listOf("t1","t5","t9"),
            listOf("t2","t6","t8"),
            listOf("t3","t4","t7"),
            listOf("t1","t6","t7"),
            listOf("t2","t5","t9"),
            listOf("t3","t8","t9"),
            listOf("t4","t7","t8"),
            listOf("t1","t4","t8"),
            listOf("t2","t3","t9"),
            listOf("t5","t6","t7"),
            listOf("t1","t3","t8"),
            listOf("t2","t4","t6"),
            listOf("t3","t5","t7"),
            listOf("t4","t6","t9"),
            listOf("t1","t7","t9"),
            listOf("t2","t8","t9"),
            listOf("t3","t6","t8"),
            listOf("t4","t5","t9"),
            listOf("t5","t7","t8"),
            listOf("t6","t8","t9"),
            listOf("t1","t2","t9")
        )
    }
}