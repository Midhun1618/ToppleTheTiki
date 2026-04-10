package com.voxcom.topplethetiki.domain.usecase


import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.data.repository.RoomRepository

class CreateRoomUseCase(
    private val repository: RoomRepository = RoomRepository()
) {
    fun execute(hostId: String, player: Player, onSuccess: (String) -> Unit) {
        repository.createRoom(hostId, player, onSuccess)
    }
}