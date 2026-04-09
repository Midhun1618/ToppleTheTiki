package com.voxcom.topplethetiki.data.remote

import com.google.firebase.auth.GoogleAuthProvider
import com.voxcom.topplethetiki.data.model.User

class AuthService {

    private val auth = FirebaseService.auth
    private val usersRef = FirebaseService.usersRef

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun saveUser(user: User, onComplete: () -> Unit) {
        usersRef.child(user.uid).setValue(user)
            .addOnSuccessListener { onComplete() }
    }

    fun checkUsernameExists(username: String, callback: (Boolean) -> Unit) {
        usersRef.orderByChild("username").equalTo(username)
            .get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.exists())
            }
    }
}