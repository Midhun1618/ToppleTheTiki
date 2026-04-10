package com.voxcom.topplethetiki.ui.game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.voxcom.topplethetiki.databinding.ActivityGameBinding
import com.voxcom.topplethetiki.R

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var adapter: GameAdapter
    private lateinit var cardAdapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roomId = intent.getStringExtra("ROOM_ID") ?: ""

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        setupUI()
        observeData()

        // 🔥 INIT AFTER OBSERVE
        viewModel.init(roomId)
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
    }

    private fun observeData() {

        viewModel.tikiStack.observe(this) {
            println("🎯 UI updating stack: $it")
            adapter.submitList(it)
        }

        viewModel.cards.observe(this) {
            cardAdapter.submitList(it)
        }

        viewModel.currentTurn.observe(this) {
            binding.tvTurn.text = "Turn: $it"
        }
        val uid = FirebaseAuth.getInstance().uid ?: return

        viewModel.gameState.observe(this) { state ->

            val secret = state.playerSecrets[uid]

            if (secret != null && secret.size == 3) {
                setSecretImages(secret)
            }
        }
    }
    private fun setSecretImages(secret: List<String>) {

        val map = mapOf(
            "t1" to R.drawable.t1,
            "t2" to R.drawable.t2,
            "t3" to R.drawable.t3,
            "t4" to R.drawable.t4,
            "t5" to R.drawable.t5,
            "t6" to R.drawable.t6,
            "t7" to R.drawable.t7,
            "t8" to R.drawable.t8,
            "t9" to R.drawable.t9
        )

        binding.secret1.setImageResource(map[secret[0]] ?: R.drawable.t1)
        binding.secret2.setImageResource(map[secret[1]] ?: R.drawable.t1)
        binding.secret3.setImageResource(map[secret[2]] ?: R.drawable.t1)
    }

}