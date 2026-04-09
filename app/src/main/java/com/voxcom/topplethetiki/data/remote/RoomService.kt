package com.voxcom.topplethetiki.data.remote


import com.google.firebase.database.*
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.data.model.Room
import java.util.*

class RoomService {

    private val roomsRef = FirebaseService.roomsRef

    fun createRoom(hostId: String, player: Player, onSuccess: (String) -> Unit) {
        val roomId = UUID.randomUUID().toString().substring(0, 6)

        val room = Room(
            roomId = roomId,
            hostId = hostId,
            players = mapOf(hostId to player)
        )

        roomsRef.child(roomId).setValue(room)
            .addOnSuccessListener {
                onSuccess(roomId)
            }
    }

    fun joinRoom(roomId: String, player: Player, callback: (Boolean, String?) -> Unit) {
        val roomRef = roomsRef.child(roomId)

        roomRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val room = currentData.getValue(Room::class.java) ?: return Transaction.success(currentData)

                if (room.players.size >= 4) {
                    return Transaction.abort()
                }

                val updatedPlayers = room.players.toMutableMap()
                updatedPlayers[player.uid] = player

                currentData.child("players").value = updatedPlayers

                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                if (committed) {
                    callback(true, null)
                } else {
                    callback(false, "Room full or error")
                }
            }
        })
    }

    fun observeRoom(roomId: String, listener: (Room?) -> Unit): ValueEventListener {
        val ref = roomsRef.child(roomId)

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listener(snapshot.getValue(Room::class.java))
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        ref.addValueEventListener(eventListener)
        return eventListener
    }
}