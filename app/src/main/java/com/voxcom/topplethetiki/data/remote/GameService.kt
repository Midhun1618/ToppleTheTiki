package com.voxcom.topplethetiki.data.remote


import com.google.firebase.database.*
import com.voxcom.topplethetiki.data.model.GameState
import com.voxcom.topplethetiki.data.model.PlayerAction

class GameService {

    fun initializeGame(roomId: String, gameState: GameState) {
        FirebaseService.getGameStateRef(roomId)
            .setValue(gameState)
    }

    fun observeGameState(roomId: String, listener: (GameState?) -> Unit): ValueEventListener {
        val ref = FirebaseService.getGameStateRef(roomId)

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listener(snapshot.getValue(GameState::class.java))
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        ref.addValueEventListener(eventListener)
        return eventListener
    }

    fun lockTurn(roomId: String, action: PlayerAction) {
        val ref = FirebaseService.getGameStateRef(roomId)

        ref.child("turnLocked").setValue(true)
        ref.child("currentPlayerAction").setValue(action)
    }

    fun updateTikiStack(roomId: String, newStack: List<String>, newVersion: Int) {
        val ref = FirebaseService.getGameStateRef(roomId)

        ref.child("tikiStack").setValue(newStack)
        ref.child("stackVersion").setValue(newVersion)
    }

    fun nextTurn(roomId: String, nextIndex: Int, nextPlayer: String) {
        val ref = FirebaseService.getGameStateRef(roomId)

        ref.child("currentTurnIndex").setValue(nextIndex)
        ref.child("currentTurn").setValue(nextPlayer)
        ref.child("turnLocked").setValue(false)
        ref.child("currentPlayerAction").setValue(null)
    }
}