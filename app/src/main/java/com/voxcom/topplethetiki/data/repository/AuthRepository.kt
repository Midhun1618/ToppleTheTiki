package com.voxcom.topplethetiki.data.repository

import com.google.firebase.database.ValueEventListener
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.data.model.Room
import com.voxcom.topplethetiki.data.remote.RoomService

class RoomRepository(
    private val roomService: RoomService = RoomService()
) {

    fun createRoom(hostId: String, player: Player, onSuccess: (String) -> Unit) {
        roomService.createRoom(hostId, player, onSuccess)
    }

    fun joinRoom(roomId: String, player: Player, callback: (Boolean, String?) -> Unit) {
        roomService.joinRoom(roomId, player, callback)
    }

    fun observeRoom(roomId: String, listener: (Room?) -> Unit): ValueEventListener {
        return roomService.observeRoom(roomId, listener)
    }
}