package com.voxcom.topplethetiki.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

object FirebaseService {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val usersRef: DatabaseReference = database.getReference("users")
    val roomsRef: DatabaseReference = database.getReference("rooms")
    val gameRef: DatabaseReference = database.getReference("game")

    fun getRoomRef(roomId: String): DatabaseReference {
        return roomsRef.child(roomId)
    }

    fun getGameStateRef(roomId: String): DatabaseReference {
        return roomsRef.child(roomId).child("gameState")
    }
}