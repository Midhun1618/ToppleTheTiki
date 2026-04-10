package com.voxcom.topplethetiki.domain.usecase

class ApplyActionUseCase {

    fun applyAction(
        stack: List<String>,
        action: String,
        tiki: String
    ): List<String> {

        val newStack = stack.toMutableList()
        val index = newStack.indexOf(tiki)

        if (index == -1) return stack

        when (action) {

            "up1" -> {
                if (index > 0) {
                    newStack.removeAt(index)
                    newStack.add(index - 1, tiki)
                }
            }

            "up2" -> {
                val newIndex = (index - 2).coerceAtLeast(0)
                newStack.removeAt(index)
                newStack.add(newIndex, tiki)
            }

            "up3" -> {
                val newIndex = (index - 3).coerceAtLeast(0)
                newStack.removeAt(index)
                newStack.add(newIndex, tiki)
            }

            "top" -> {
                newStack.remove(tiki)
                newStack.add(0, tiki)
            }

            "bottom" -> {
                newStack.remove(tiki)
                newStack.add(tiki)
            }

            "toss" -> {
                newStack.remove(tiki)
            }
        }

        return newStack
    }
}