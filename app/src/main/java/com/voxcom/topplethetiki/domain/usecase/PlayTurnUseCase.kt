package com.voxcom.topplethetiki.domain.usecase

import com.voxcom.topplethetiki.data.model.PlayerAction
import com.voxcom.topplethetiki.data.repository.GameRepository

class PlayTurnUseCase(
    private val repository: GameRepository = GameRepository()
) {

    fun lockTurn(roomId: String, action: PlayerAction) {
        repository.lockTurn(roomId, action)
    }
}