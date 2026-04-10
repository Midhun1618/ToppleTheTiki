package com.voxcom.topplethetiki.ui.room

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.databinding.ActivityRoomBinding

class RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomBinding
    private lateinit var adapter: PlayerAdapter

    private lateinit var database: DatabaseReference

    private var roomId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getStringExtra("ROOM_ID") ?: ""
        binding.tvRoomCode.text = "Room: $roomId"

        setupRecycler()
        listenToPlayers()
    }

    private fun setupRecycler() {

        adapter = PlayerAdapter { player ->
            // 🔥 REAL LOGIC (for now simple)
            Toast.makeText(this, "Clicked: ${player.username}", Toast.LENGTH_SHORT).show()
        }

        binding.rvPlayers.layoutManager = LinearLayoutManager(this)
        binding.rvPlayers.adapter = adapter
    }

    // 🔥 Firebase real-time listener
    private fun listenToPlayers() {

        database = FirebaseDatabase.getInstance()
            .getReference("rooms")
            .child(roomId)
            .child("players")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val playerList = mutableListOf<Player>()

                for (child in snapshot.children) {
                    val player = child.getValue(Player::class.java)
                    if (player != null) {
                        playerList.add(player)
                    }
                }

                adapter.updatePlayers(playerList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RoomActivity, "Failed to load players", Toast.LENGTH_SHORT).show()
            }
        })
    }
}