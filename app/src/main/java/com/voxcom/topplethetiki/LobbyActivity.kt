package com.voxcom.topplethetiki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LobbyActivity : AppCompatActivity() {
    private lateinit var et : EditText
    private lateinit var btn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lobby)

        btn = findViewById(R.id.goBtn)
        et = findViewById(R.id.inputNos)

        val nos = et.text.toString().trim()
        btn.setOnClickListener{
                    val intent = Intent(this, LoadingActivity::class.java)
                    intent.putExtra("player_nos",nos)
                    startActivity(intent)


        }


    }
}