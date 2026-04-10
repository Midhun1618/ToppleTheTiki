package com.voxcom.topplethetiki.ui.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voxcom.topplethetiki.databinding.ActivityRoundResultBinding

class RoundResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoundResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoundResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvResult.text = "Round Completed!"
    }
}