package com.example.rag.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rag.data.GameRepository
import com.example.rag.data.RagResponse
import com.example.rag.rag.GameRagEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val currentResponse: RagResponse? = null,
    val history: List<RagResponse> = emptyList()
)

class GameViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val ragEngine = GameRagEngine(GameRepository(application))

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    fun submitQuery(queryText: String = _uiState.value.query) {
        if (queryText.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, query = queryText) }
            
            delay(400)

            val response = ragEngine.ask(queryText)

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    currentResponse = response,
                    history = listOf(response) + state.history
                )
            }
        }
    }
}
