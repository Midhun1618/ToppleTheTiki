package com.voxcom.topplethetiki.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogleSignIn()

        binding.btnGoogleLogin.setOnClickListener {
            signIn()
        }
    }

    private fun setupGoogleSignIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, options)
    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
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

            runOnUiThread {
                if (success) {

                    val user = FirebaseAuth.getInstance().currentUser

                    if (user != null) {
                        goToLobby()
                    }

                } else {
                    Toast.makeText(this, error ?: "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToLobby() {
        startActivity(Intent(this, LobbyActivity::class.java))
        finish()
    }
}