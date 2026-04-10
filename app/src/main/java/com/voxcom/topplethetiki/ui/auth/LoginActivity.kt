package com.voxcom.topplethetiki.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.FirebaseAuth
import com.voxcom.topplethetiki.R
import com.voxcom.topplethetiki.data.repository.AuthRepository
import com.voxcom.topplethetiki.databinding.ActivityLoginBinding
import com.voxcom.topplethetiki.ui.lobby.LobbyActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    private val authRepository = AuthRepository()

    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogleSignIn()

        binding.btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun setupGoogleSignIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from Firebase
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, options)
    }

    private fun signInWithGoogle() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            if (task.isSuccessful) {
                val account = task.result
                val idToken = account.idToken

                if (idToken != null) {
                    firebaseAuth(idToken)
                }
            } else {
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuth(idToken: String) {
        authRepository.signInWithGoogle(idToken) { success, error ->

            if (success) {

                val uid = FirebaseAuth.getInstance().currentUser?.uid

                if (uid != null) {
                    checkUserExists(uid)
                }

            } else {
                Toast.makeText(this, error ?: "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUserExists(uid: String) {

        // Check if username exists in DB
        authRepository.checkUsernameExists(uid) { exists ->

            if (exists) {
                startActivity(Intent(this, LobbyActivity::class.java))
            } else {
                startActivity(Intent(this, UsernameActivity::class.java))
            }

            finish()
        }
    }
}}