package com.voxcom.topplethetiki.ui.game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.voxcom.topplethetiki.databinding.ActivityGameBinding
import com.voxcom.topplethetiki.R
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.ui.room.PlayerAdapter

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var adapter: GameAdapter
    private lateinit var cardAdapter: CardAdapter
    private lateinit var playerAdapter: PlayerAdapter
    private val playerList = mutableListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roomId = intent.getStringExtra("ROOM_ID") ?: ""

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        setupUI()
        observeData()

        viewModel.init(roomId)
        listenToPlayers(roomId)
    }

    private fun setupUI() {

        adapter = GameAdapter { tiki ->
            viewModel.onTikiSelected(tiki)
        }
        cardAdapter = CardAdapter { card ->
            viewModel.onCardSelected(card)
        }

        binding.rvCards.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvCards.adapter = cardAdapter

        binding.rvTikiStack.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvTikiStack.adapter = adapter

        binding.btnPlayAction.setOnClickListener {
            viewModel.playTurn()
        }
        playerAdapter = PlayerAdapter { }

        binding.rvPlayers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvPlayers.adapter = playerAdapter
    }

    private fun observeData() {

        viewModel.tikiStack.observe(this) {
            println("🎯 UI updating stack: $it")
            adapter.submitList(it)
        }

        viewModel.cards.observe(this) {
            cardAdapter.submitList(it)
        }

        val uid = FirebaseAuth.getInstance().uid ?: return

        viewModel.gameState.observe(this) { state ->

            val secret = state.playerSecrets[uid]

            if (secret != null && secret.size == 3) {
                setSecretImages(secret)
            }
        }
        viewModel.currentTurn.observe(this) { uid ->
            playerAdapter.setCurrentTurn(uid)
        }
    }
    private fun setSecretImages(secret: List<String>) {

        val map = mapOf(
            "t1" to R.drawable.paint_tiki_1,
            "t2" to R.drawable.paint_tiki_2,
            "t3" to R.drawable.paint_tiki_3,
            "t4" to R.drawable.paint_tiki_4,
            "t5" to R.drawable.paint_tiki_5,
            "t6" to R.drawable.paint_tiki_6,
            "t7" to R.drawable.paint_tiki_7,
            "t8" to R.drawable.paint_tiki_8,
            "t9" to R.drawable.paint_tiki_9
        )

        binding.secret1.setImageResource(map[secret[0]] ?: R.drawable.t1)
        binding.secret2.setImageResource(map[secret[1]] ?: R.drawable.t1)
        binding.secret3.setImageResource(map[secret[2]] ?: R.drawable.t1)
    }
    private fun listenToPlayers(roomId: String) {

        val ref = com.google.firebase.database.FirebaseDatabase.getInstance()
            .getReference("rooms")
            .child(roomId)
            .child("players")

        ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {

            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                val list = mutableListOf<Player>()

                for (child in snapshot.children) {
                    val player = child.getValue(Player::class.java)?.copy(
                        uid = child.key ?: ""
                    )

                    if (player != null) list.add(player)
                }

                playerList.clear()
                playerList.addAll(list)

                playerAdapter.updatePlayers(playerList)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
        })
    }

}