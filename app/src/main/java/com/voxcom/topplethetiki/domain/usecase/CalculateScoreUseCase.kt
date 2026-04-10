package com.voxcom.topplethetiki.domain.usecase

class CalculateScoreUseCase {

    fun calculateScore(
        finalStack: List<String>,
        secretCard: List<String>
    ): Int {

        val finalThree = finalStack.take(3)

        var score = 0

        for (tiki in secretCard) {
            if (finalThree.contains(tiki)) {
                score += 1
            }
        }

        // Bonus
        if (score == 3) {
            score += 2
        }

        return score
    }
}