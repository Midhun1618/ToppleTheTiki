package com.voxcom.topplethetiki.domain.usecase

import kotlin.random.Random

class AssignCardsUseCase {

    private val tikiList = listOf("t1","t2","t3","t4","t5","t6","t7","t8","t9")

    fun generateSecretCards(): MutableList<List<String>> {
        val cards = mutableListOf<List<String>>()

        while (cards.size < 27) {
            val card = tikiList.shuffled().take(3)
            if (!cards.contains(card)) {
                cards.add(card)
            }
        }
        return cards
    }

    fun assignToPlayers(playerIds: List<String>): Map<String, List<String>> {
        val pool = generateSecretCards().shuffled().toMutableList()
        val result = mutableMapOf<String, List<String>>()

        for (id in playerIds) {
            val card = pool.removeAt(Random.nextInt(pool.size))
            result[id] = card
        }

        return result
    }
}