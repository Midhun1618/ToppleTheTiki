package com.voxcom.topplethetiki.data.repository


import com.voxcom.topplethetiki.data.model.User
import com.voxcom.topplethetiki.data.remote.AuthService

class AuthRepository(
    private val authService: AuthService = AuthService()
) {

    fun isLoggedIn(): Boolean {
        return authService.isLoggedIn()
    }

    fun getCurrentUserId(): String? {
        return authService.getCurrentUserId()
    }

    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        authService.signInWithGoogle(idToken, onResult)
    }

    fun saveUser(user: User, onComplete: () -> Unit) {
        authService.saveUser(user, onComplete)
    }

    fun checkUsernameExists(username: String, callback: (Boolean) -> Unit) {
        authService.checkUsernameExists(username, callback)
    }
}