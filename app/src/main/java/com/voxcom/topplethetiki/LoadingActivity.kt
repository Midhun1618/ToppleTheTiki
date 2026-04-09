package com.voxcom.topplethetiki

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoadingActivity : AppCompatActivity() {
    private lateinit  var tx : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)
        val number = intent.getStringExtra("player_nos")
        tx.text ="Number of players is ${number}"
    }
}