package com.voxcom.topplethetiki.ui.lobby

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.data.repository.AuthRepository
import com.voxcom.topplethetiki.data.repository.RoomRepository
import com.voxcom.topplethetiki.databinding.ActivityLobbyBinding
import com.voxcom.topplethetiki.ui.room.RoomActivity
import com.voxcom.topplethetiki.utils.GameUtils

class LobbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLobbyBinding

    private val authRepository = AuthRepository()
    private val roomRepository = RoomRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClicks()
    }

    private fun setupClicks() {

        // 🎮 CREATE ROOM
        binding.btnCreateRoom.setOnClickListener {

            val uid = authRepository.getCurrentUserId()

            if (uid == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnCreateRoom.isEnabled = false

            fetchUsername(uid) { username ->

                val player = Player(
                    uid = uid,
                    username = username,
                    avatar = ""
                )

                roomRepository.createRoom(uid, player) { roomId ->

                    runOnUiThread {
                        binding.btnCreateRoom.isEnabled = true

                        val intent = Intent(this, RoomActivity::class.java)
                        intent.putExtra("ROOM_ID", roomId)
                        startActivity(intent)
                    }
                }
            }
        }

        // 🔗 JOIN ROOM
        binding.btnJoinRoom.setOnClickListener {
            if(binding.etRoomCode.visibility == View.VISIBLE){

            val roomId = binding.etRoomCode.text.toString().trim()

            if (roomId.isEmpty()) {
                binding.etRoomCode.error = "Enter room code"
                return@setOnClickListener
            }

            val uid = authRepository.getCurrentUserId()

            if (uid == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fetchUsername(uid) { username ->

                val player = Player(
                    uid = uid,
                    username = username,
                    avatar = ""
                )

                roomRepository.joinRoom(roomId, player) { success, error ->

                    runOnUiThread {
                        if (success) {
                            val intent = Intent(this, RoomActivity::class.java)
                            intent.putExtra("ROOM_ID", roomId)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, error ?: "Failed to join room", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            }else{
                binding.etRoomCode.visibility = View.VISIBLE
                return@setOnClickListener
            }
        }

    }

    // 🔥 Fetch username from Firebase
    private fun fetchUsername(uid: String, callback: (String) -> Unit) {

        val userRef = com.google.firebase.database.FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(uid)
            .child("username")

        userRef.get().addOnSuccessListener {
            val username = it.getValue(String::class.java) ?: "Player"
            callback(username)
        }.addOnFailureListener {
            callback("Player")
        }
    }
}