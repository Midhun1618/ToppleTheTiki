package com.voxcom.topplethetiki.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
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
    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    fun init(roomId: String) {
        this.roomId = roomId

        // 🔥 TEMP FALLBACK (so UI never empty)
        _tikiStack.value = listOf(
            "t1","t2","t3","t4","t5","t6","t7","t8","t9"
        )
        _currentTurn.value = "Waiting..."

        repository.observeGameState(roomId) { state ->

            if (state == null) {
                println("⚠️ GameState is NULL")
                return@observeGameState
            }

            println("🔥 GameState received: $state")

            currentState = state

            _tikiStack.postValue(state.tikiStack)
            _currentTurn.postValue(state.currentTurn)
        }
        repository.observeGameState(roomId) { state ->

            if (state == null) return@observeGameState

            currentState = state

            _gameState.postValue(state)   // ✅ ADD THIS

            _tikiStack.postValue(state.tikiStack)
            _currentTurn.postValue(state.currentTurn)
        }
    }

    fun onTikiSelected(tiki: String) {
        selectedTiki = tiki
    }

    fun playTurn() {

        val state = currentState
        val tiki = selectedTiki

        if (state == null || tiki == null) {
            println("❌ Cannot play turn (state or tiki missing)")
            return
        }

        val playerId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val action = "up1" // TEMP

        gameManager.playTurn(
            roomId,
            state,
            playerId,
            action,
            tiki
        )
    }
}