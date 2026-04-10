package com.voxcom.topplethetiki.ui.room

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.databinding.ActivityRoomBinding
import com.voxcom.topplethetiki.ui.loading.LoadingActivity

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
        setupReadyButton()
        setupStartButton()
        listenForGameStart()
    }

    // 🎮 Recycler setup
    private fun setupRecycler() {

        // ✅ Player list
        adapter = PlayerAdapter { player ->
            Toast.makeText(this, player.username, Toast.LENGTH_SHORT).show()
        }

        binding.rvPlayers.layoutManager = LinearLayoutManager(this)
        binding.rvPlayers.adapter = adapter

        // ✅ Avatar preview (optional)
        binding.rvAvatars.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvAvatars.adapter = AvatarAdapter()
    }

    // 🔥 Listen players + auto avatar assign
    private fun listenToPlayers() {

        database = FirebaseDatabase.getInstance()
            .getReference("rooms")
            .child(roomId)
            .child("players")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val playerList = mutableListOf<Player>()

                // ✅ Stable ordering
                val sortedPlayers = snapshot.children.sortedBy { it.key }

                for ((index, child) in sortedPlayers.withIndex()) {

                    val player = child.getValue(Player::class.java)

                    if (player != null) {

                        var finalPlayer = player

                        // 🔥 AUTO ASSIGN AVATAR
                        if (player.avatar.isEmpty()) {
                            val avatar = "avatar_${index + 1}"
                            child.ref.child("avatar").setValue(avatar)
                            finalPlayer = player.copy(avatar = avatar)
                        }

                        playerList.add(finalPlayer)
                    }
                }

                adapter.updatePlayers(playerList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RoomActivity, "Failed to load players", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ✅ READY BUTTON
    private fun setupReadyButton() {

        binding.btnReady.setOnClickListener {

            val uid = FirebaseAuth.getInstance().uid ?: return@setOnClickListener

            val playerRef = FirebaseDatabase.getInstance()
                .getReference("rooms")
                .child(roomId)
                .child("players")
                .child(uid)

            playerRef.child("ready").setValue(true)

            Toast.makeText(this, "You are ready ✅", Toast.LENGTH_SHORT).show()
        }
    }

    // 🚀 START GAME (HOST ONLY)
    private fun setupStartButton() {

        binding.btnStartGame.setOnClickListener {

            val roomRef = FirebaseDatabase.getInstance()
                .getReference("rooms")
                .child(roomId)

            roomRef.get().addOnSuccessListener { snapshot ->

                val hostId = snapshot.child("hostId").getValue(String::class.java)
                val currentUser = FirebaseAuth.getInstance().uid

                // ❌ Only host can start
                if (currentUser != hostId) {
                    Toast.makeText(this, "Only host can start", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val players = snapshot.child("players")

                var allReady = true

                for (player in players.children) {
                    val ready = player.child("ready").getValue(Boolean::class.java) ?: false
                    if (!ready) {
                        allReady = false
                        break
                    }
                }

                if (!allReady) {
                    Toast.makeText(this, "All players not ready", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                // 🔥 START GAME
                roomRef.child("status").setValue("starting")
            }
        }
    }

    // 🎬 LISTEN FOR GAME START
    private fun listenForGameStart() {

        val statusRef = FirebaseDatabase.getInstance()
            .getReference("rooms")
            .child(roomId)
            .child("status")

        statusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val status = snapshot.getValue(String::class.java)

                if (status == "starting") {

                    Toast.makeText(this@RoomActivity, "Game Starting 🚀", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RoomActivity, LoadingActivity::class.java)
                    intent.putExtra("ROOM_ID", roomId)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}