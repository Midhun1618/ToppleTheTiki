package com.voxcom.topplethetiki.ui.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voxcom.topplethetiki.databinding.ActivityFinalResultBinding

class FinalResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinalResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWinner.text = "Winner!"
    }
}