package com.voxcom.topplethetiki.ui.loading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.voxcom.topplethetiki.data.model.GameState
import com.voxcom.topplethetiki.ui.game.GameActivity
import com.voxcom.topplethetiki.utils.GameUtils

class LoadingActivity : AppCompatActivity() {

    private var roomId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomId = intent.getStringExtra("ROOM_ID") ?: ""

        createGameState()
    }

    private fun createGameState() {

        val roomRef = FirebaseDatabase.getInstance()
            .getReference("rooms")
            .child(roomId)

        roomRef.child("players").get().addOnSuccessListener { snapshot ->

            val playerIds = mutableListOf<String>()

            for (child in snapshot.children) {
                child.key?.let { playerIds.add(it) }
            }

            if (playerIds.isEmpty()) return@addOnSuccessListener

            // 🔥 TURN ORDER
            val turnOrder = playerIds.shuffled()

            val firstTurnIndex = 0
            val firstPlayer = turnOrder[firstTurnIndex]

            // 🔥 TIKI STACK (ORDERED)
            val tikiStack = GameUtils.getInitialTikiStack()

            val gameState = GameState(
                roundNumber = 1,
                maxRounds = playerIds.size,

                tikiStack = tikiStack,
                stackVersion = 0,

                turnOrder = turnOrder,
                currentTurnIndex = 0,
                currentTurn = firstPlayer,

                turnLocked = false,

                currentPlayerAction = null,

                playerSecrets =playerSecrets // 🔥 ADD THIS
            )

            // 🔥 PUSH TO FIREBASE
            roomRef.child("gameState").setValue(gameState)
                .addOnSuccessListener {

                    Handler(Looper.getMainLooper()).postDelayed({

                        val intent = Intent(this, GameActivity::class.java)
                        intent.putExtra("ROOM_ID", roomId)
                        startActivity(intent)
                        finish()

                    }, 1500)
                }
        }
        // 🔥 SECRET CARD ASSIGN
        val secretCards = GameUtils.getSecretCards().shuffled()

        val playerSecrets = mutableMapOf<String, List<String>>()

        for ((index, playerId) in turnOrder.withIndex()) {
            playerSecrets[playerId] = secretCards[index]
        }
    }
}