package com.voxcom.topplethetiki.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.voxcom.topplethetiki.R
import com.voxcom.topplethetiki.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Replicate the Pulsing Idol from Compose
        // Make sure res/anim/pulse_idol.xml exists
        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse_idol)
        binding.tikiIdol.startAnimation(pulse)

        // 2. Simple logic for the progress bar (0 to 100 in 3 seconds)
        var currentProgress = 0
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (currentProgress <= 100) {
                    binding.splashProgress.progress = currentProgress
                    currentProgress++
                    handler.postDelayed(this, 30) // Update every 30ms
                }
            }
        }
        handler.post(runnable)

        // 3. Navigate to Login after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3500)
    }
}