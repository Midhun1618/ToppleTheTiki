package com.voxcom.topplethetiki.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voxcom.topplethetiki.data.model.GameState
import com.voxcom.topplethetiki.domain.manager.GameManager
import com.voxcom.topplethetiki.data.repository.GameRepository

class GameViewModel : ViewModel() {

    private val repository = GameRepository()
    private val gameManager = GameManager()

    private var roomId: String = ""
    private var currentState: GameState? = null
    private var selectedTiki: String? = null

    private val _tikiStack = MutableLiveData<List<String>>()
    val tikiStack: LiveData<List<String>> = _tikiStack

    private val _currentTurn = MutableLiveData<String>()
    val currentTurn: LiveData<String> = _currentTurn

    fun init(roomId: String) {
        this.roomId = roomId

        repository.observeGameState(roomId) { state ->
            state?.let {
                currentState = it
                _tikiStack.postValue(it.tikiStack)
                _currentTurn.postValue(it.currentTurn)
            }
        }
    }

    fun onTikiSelected(tiki: String) {
        selectedTiki = tiki
    }

    fun playTurn() {

        val state = currentState ?: return
        val tiki = selectedTiki ?: return

        val playerId = com.google.firebase.auth.FirebaseAuth
            .getInstance().currentUser?.uid ?: return

        val action = "up1" // TEMP (later selectable)

        gameManager.playTurn(
            roomId,
            state,
            playerId,
            action,
            tiki
        )
    }
}