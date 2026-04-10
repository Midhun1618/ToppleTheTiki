package com.voxcom.topplethetiki.ui.loading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.voxcom.topplethetiki.databinding.ActivityLoadingBinding
import com.voxcom.topplethetiki.ui.game.GameActivity

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roomId = intent.getStringExtra("ROOM_ID") ?: ""

        // Simulate loading (later replace with real game init)
        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("ROOM_ID", roomId)
            startActivity(intent)
            finish()

        }, 2000)
    }
}