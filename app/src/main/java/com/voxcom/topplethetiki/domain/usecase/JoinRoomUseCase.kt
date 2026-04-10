package com.voxcom.topplethetiki.domain.usecase

import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.data.repository.RoomRepository

class JoinRoomUseCase(
    private val repository: RoomRepository = RoomRepository()
) {
    fun execute(roomId: String, player: Player, callback: (Boolean, String?) -> Unit) {
        repository.joinRoom(roomId, player, callback)
    }
}