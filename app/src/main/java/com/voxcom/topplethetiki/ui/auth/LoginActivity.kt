package com.voxcom.topplethetiki.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.voxcom.topplethetiki.R
import com.voxcom.topplethetiki.databinding.ActivityLoginBinding
import com.voxcom.topplethetiki.ui.lobby.LobbyActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private var currentUid: String? = null
    private var isNewUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()

        // ✅ Auto login
        if (auth.currentUser != null) {
            goToLobby()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnContinue.setOnClickListener {

            val username = binding.etUsername.text.toString().trim()

            if (username.isEmpty()) {
                binding.etUsername.error = "Enter username"
                return@setOnClickListener
            }

            saveUsername(username)
        }
    }

    // 🔐 Google Sign-In
    private fun signInWithGoogle() {

        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                binding.loadingLayout.visibility = View.VISIBLE
                binding.tvLoading.text = "Opening Google..."

                val result = credentialManager.getCredential(
                    context = this@LoginActivity,
                    request = request
                )

                val credential = result.credential

                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    firebaseAuthWithGoogle(googleCredential)

                } else {
                    Toast.makeText(this@LoginActivity, "Unexpected credential", Toast.LENGTH_SHORT).show()
                }

            } catch (e: GetCredentialException) {
                Toast.makeText(this@LoginActivity, "Sign-in cancelled", Toast.LENGTH_SHORT).show()
                binding.loadingLayout.visibility = View.GONE
            }
        }
    }

    // 🔥 Firebase Auth
    private fun firebaseAuthWithGoogle(credential: GoogleIdTokenCredential) {

        binding.tvLoading.text = "Authenticating..."

        val firebaseCredential =
            GoogleAuthProvider.getCredential(credential.idToken, null)

        auth.signInWithCredential(firebaseCredential)
            .addOnSuccessListener {

                val uid = auth.currentUser!!.uid
                currentUid = uid

                checkUserExists(uid)
            }
            .addOnFailureListener {
                binding.tvLoading.text = "Login failed"
                Toast.makeText(this, "Auth Failed", Toast.LENGTH_SHORT).show()
            }
    }

    // 🔍 Check user in Firebase
    private fun checkUserExists(uid: String) {

        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)

        binding.tvLoading.text = "Checking user..."

        ref.get().addOnSuccessListener {

            if (it.exists() && it.child("username").exists()) {
                goToLobby()
            } else {
                isNewUser = true
                showUsernameInput()
            }
        }
    }

    // ✏️ Show username input
    private fun showUsernameInput() {

        binding.loadingLayout.visibility = View.GONE

        binding.etUsername.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.VISIBLE

        binding.tvLoading.text = "Enter your username"
    }

    // 💾 Save username
    private fun saveUsername(username: String) {

        val uid = currentUid ?: return

        binding.loadingLayout.visibility = View.VISIBLE
        binding.tvLoading.text = "Saving username..."

        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)

        val userMap = mapOf(
            "username" to username
        )

        ref.setValue(userMap).addOnSuccessListener {
            goToLobby()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save username", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToLobby() {
        startActivity(Intent(this, LobbyActivity::class.java))
        finish()
    }
}