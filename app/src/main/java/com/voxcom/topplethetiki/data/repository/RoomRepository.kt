package com.voxcom.topplethetiki.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.utils.GameUtils

class RoomRepository {

    private val db = FirebaseDatabase.getInstance().reference

    // 🔥 CREATE ROOM
    fun createRoom(
        hostId: String,
        player: Player,
        callback: (String) -> Unit
    ) {

        val roomId = GameUtils.generateRoomCode()

        val roomRef = db.child("rooms").child(roomId)

        val roomData = mapOf(
            "roomId" to roomId,
            "hostId" to hostId,
            "status" to "waiting"
        )

        roomRef.setValue(roomData)
            .addOnSuccessListener {

                // ✅ Add host as first player
                roomRef.child("players")
                    .child(player.uid)
                    .setValue(player)
                    .addOnSuccessListener {
                        callback(roomId) // 🔥 RETURN ROOM CODE
                    }
                    .addOnFailureListener {
                        callback("") // failure
                    }
            }
            .addOnFailureListener {
                callback("")
            }
    }

    // 🔗 JOIN ROOM
    fun joinRoom(
        roomId: String,
        player: Player,
        callback: (Boolean, String?) -> Unit
    ) {

        val roomRef = db.child("rooms").child(roomId)

        roomRef.get()
            .addOnSuccessListener { snapshot ->

                if (!snapshot.exists()) {
                    callback(false, "Room not found")
                    return@addOnSuccessListener
                }

                val playersNode = snapshot.child("players")
                val playerCount = playersNode.childrenCount

                if (playerCount >= 4) {
                    callback(false, "Room full")
                    return@addOnSuccessListener
                }

                roomRef.child("players")
                    .child(player.uid)
                    .setValue(player)
                    .addOnSuccessListener {
                        callback(true, null)
                    }
                    .addOnFailureListener {
                        callback(false, "Failed to join")
                    }
            }
            .addOnFailureListener {
                callback(false, "Error fetching room")
            }
    }
}