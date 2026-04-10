package com.voxcom.topplethetiki.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.voxcom.topplethetiki.data.model.User
import com.voxcom.topplethetiki.data.remote.AuthService

class AuthRepository(
    private val authService: AuthService = AuthService()
) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    // 🔐 Check login
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // 👤 Get UID
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // 🔑 Google Sign-In
    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        authService.signInWithGoogle(idToken, onResult)
    }

    // 💾 Save user in DB
    fun saveUser(user: User, onComplete: () -> Unit) {

        val uid = user.uid

        db.child("users")
            .child(uid)
            .setValue(user)
            .addOnSuccessListener {
                onComplete()
            }
    }

    // 🔍 Get username (VERY IMPORTANT FOR YOUR FLOW)
    fun getUsername(uid: String, callback: (String) -> Unit) {

        db.child("users")
            .child(uid)
            .child("username")
            .get()
            .addOnSuccessListener {
                val username = it.getValue(String::class.java) ?: "Player"
                callback(username)
            }
            .addOnFailureListener {
                callback("Player")
            }
    }

    // 🔍 Check if username exists (for uniqueness)
    fun checkUsernameExists(username: String, callback: (Boolean) -> Unit) {

        db.child("users")
            .get()
            .addOnSuccessListener { snapshot ->

                var exists = false

                for (user in snapshot.children) {
                    val name = user.child("username").getValue(String::class.java)
                    if (name == username) {
                        exists = true
                        break
                    }
                }

                callback(exists)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}