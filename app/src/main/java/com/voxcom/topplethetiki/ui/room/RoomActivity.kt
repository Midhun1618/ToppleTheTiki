package com.voxcom.topplethetiki.ui.room

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.voxcom.topplethetiki.databinding.ActivityRoomBinding
import com.voxcom.topplethetiki.data.repository.RoomRepository
import com.voxcom.topplethetiki.ui.loading.LoadingActivity

class RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomBinding
    private val roomRepository = RoomRepository()

    private lateinit var roomId: String

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var avatarAdapter: AvatarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getStringExtra("ROOM_ID") ?: ""

        setupUI()
        observeRoom()
    }

    private fun setupUI() {

        binding.tvRoomCode.text = "Room: $roomId"

        // Player list
        playerAdapter = PlayerAdapter()
        binding.rvPlayers.layoutManager = LinearLayoutManager(this)
        binding.rvPlayers.adapter = playerAdapter

        // Avatar list
        avatarAdapter = AvatarAdapter()
        binding.rvAvatars.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvAvatars.adapter = avatarAdapter

        binding.btnStartGame.setOnClickListener {
            startGame()
        }
    }

    private fun observeRoom() {

        roomRepository.observeRoom(roomId) { room ->

            room?.let {
                val players = it.players.values.toList()

                runOnUiThread {
                    playerAdapter.submitList(players)
                }
            }
        }
    }

    private fun startGame() {
        val intent = Intent(this, LoadingActivity::class.java)
        intent.putExtra("ROOM_ID", roomId)
        startActivity(intent)
    }
}