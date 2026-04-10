package com.voxcom.topplethetiki.ui.game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.voxcom.topplethetiki.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var adapter: GameAdapter

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

        viewModel.currentTurn.observe(this) {
            binding.tvTurn.text = "Turn: $it"
        }
    }
}