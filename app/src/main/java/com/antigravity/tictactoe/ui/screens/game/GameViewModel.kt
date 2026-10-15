package com.antigravity.tictactoe.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antigravity.tictactoe.TicTacToeApp
import com.antigravity.tictactoe.domain.ai.TicTacToeAiEngine
import com.antigravity.tictactoe.domain.game.TicTacToeGameEngine
import com.antigravity.tictactoe.domain.model.AppSettings
import com.antigravity.tictactoe.domain.model.GameDifficulty
import com.antigravity.tictactoe.domain.model.GameMode
import com.antigravity.tictactoe.domain.model.GameStatus
import com.antigravity.tictactoe.domain.model.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameEngine: TicTacToeGameEngine = TicTacToeApp.instance.gameEngine,
    private val aiEngine: TicTacToeAiEngine = TicTacToeApp.instance.aiEngine
) : ViewModel() {

    private val preferencesRepository = TicTacToeApp.instance.preferencesRepository
    private val statisticsRepository = TicTacToeApp.instance.statisticsRepository
    private val soundManager = TicTacToeApp.instance.soundManager
    private val hapticsManager = TicTacToeApp.instance.hapticsManager

    val appSettings: StateFlow<AppSettings> = preferencesRepository.appSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var aiJob: Job? = null
    private var gameSessionId = 0

    init {
        viewModelScope.launch {
            appSettings.collect { settings ->
                soundManager.isSoundEnabled = settings.soundEnabled
                hapticsManager.isHapticsEnabled = settings.hapticsEnabled
            }
        }
    }

    fun initializeGame(mode: GameMode) {
        gameSessionId++
        aiJob?.cancel()

        _uiState.value = GameUiState(
            gameMode = mode,
            currentPlayer = Player.X,
            sessionScore = SessionScore(),
            isInteractive = true
        )

        checkAndTriggerAiMoveIfNeeded()
    }

    fun onCellClicked(index: Int) {
        val state = _uiState.value
        if (!state.isInteractive || state.isAiThinking || state.gameStatus.isFinished) {
            return
        }

        // In Single Player, ignore clicks if it's AI's turn
        val isSinglePlayer = state.gameMode is GameMode.SinglePlayer
        if (isSinglePlayer && state.currentPlayer == state.aiSymbol) {
            return
        }

        executeMove(index, state.currentPlayer)
    }

    private fun executeMove(index: Int, player: Player) {
        val currentState = _uiState.value
        val result = gameEngine.makeMove(currentState.board, index, player) ?: return

        val (updatedBoard, newStatus) = result

        // Feedback
        if (player == Player.X) soundManager.playXMove() else soundManager.playOMove()
        hapticsManager.vibrateMove()

        when (newStatus) {
            is GameStatus.Won -> {
                handleGameWon(newStatus, updatedBoard, player)
            }
            is GameStatus.Draw -> {
                handleGameDraw(updatedBoard)
            }
            is GameStatus.Ongoing -> {
                _uiState.update {
                    it.copy(
                        board = updatedBoard,
                        currentPlayer = player.opponent,
                        gameStatus = newStatus,
                        isInteractive = true
                    )
                }
                checkAndTriggerAiMoveIfNeeded()
            }
        }
    }

    private fun handleGameWon(status: GameStatus.Won, board: com.antigravity.tictactoe.domain.model.Board, winner: Player) {
        val mode = _uiState.value.gameMode
        val currentScore = _uiState.value.sessionScore

        val updatedScore = if (winner == Player.X) {
            currentScore.copy(player1Score = currentScore.player1Score + 1)
        } else {
            currentScore.copy(player2Score = currentScore.player2Score + 1)
        }

        _uiState.update {
            it.copy(
                board = board,
                gameStatus = status,
                sessionScore = updatedScore,
                isInteractive = false
            )
        }

        soundManager.playWin()
        hapticsManager.vibrateWin()

        // Record statistics
        viewModelScope.launch {
            when (mode) {
                is GameMode.SinglePlayer -> {
                    val isHumanWin = winner == mode.humanSymbol
                    val isAiWin = winner == mode.aiSymbol
                    statisticsRepository.recordSinglePlayerResult(
                        difficulty = mode.difficulty,
                        isHumanWin = isHumanWin,
                        isAiWin = isAiWin,
                        isDraw = false
                    )
                }
                is GameMode.TwoPlayers -> {
                    statisticsRepository.recordTwoPlayerResult(winner)
                }
            }
        }

        // Delay showing result dialog to enjoy the winning line & pulse animations
        viewModelScope.launch {
            delay(850)
            _uiState.update { it.copy(showResultDialog = true) }
        }
    }

    private fun handleGameDraw(board: com.antigravity.tictactoe.domain.model.Board) {
        val mode = _uiState.value.gameMode
        val currentScore = _uiState.value.sessionScore
        val updatedScore = currentScore.copy(draws = currentScore.draws + 1)

        _uiState.update {
            it.copy(
                board = board,
                gameStatus = GameStatus.Draw,
                sessionScore = updatedScore,
                isInteractive = false
            )
        }

        soundManager.playDraw()
        hapticsManager.vibrateDraw()

        viewModelScope.launch {
            when (mode) {
                is GameMode.SinglePlayer -> {
                    statisticsRepository.recordSinglePlayerResult(
                        difficulty = mode.difficulty,
                        isHumanWin = false,
                        isAiWin = false,
                        isDraw = true
                    )
                }
                is GameMode.TwoPlayers -> {
                    statisticsRepository.recordTwoPlayerResult(null)
                }
            }
        }

        viewModelScope.launch {
            delay(750)
            _uiState.update { it.copy(showResultDialog = true) }
        }
    }

    private fun checkAndTriggerAiMoveIfNeeded() {
        val state = _uiState.value
        val mode = state.gameMode as? GameMode.SinglePlayer ?: return
        if (state.gameStatus.isFinished) return

        if (state.currentPlayer == mode.aiSymbol) {
            triggerAiMove(mode.difficulty, mode.aiSymbol, mode.humanSymbol)
        }
    }

    private fun triggerAiMove(
        difficulty: GameDifficulty,
        aiSymbol: Player,
        humanSymbol: Player
    ) {
        val currentSession = gameSessionId
        aiJob?.cancel()

        aiJob = viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true, isInteractive = false) }

            // Natural thinking delay (350 - 650 ms)
            val delayMs = (350L..600L).random()
            delay(delayMs)

            if (gameSessionId != currentSession) return@launch

            val board = _uiState.value.board
            val bestMove = aiEngine.getBestMove(
                board = board,
                difficulty = difficulty,
                aiPlayer = aiSymbol,
                humanPlayer = humanSymbol
            )

            if (bestMove != null && gameSessionId == currentSession) {
                _uiState.update { it.copy(isAiThinking = false) }
                executeMove(bestMove, aiSymbol)
            } else {
                _uiState.update { it.copy(isAiThinking = false, isInteractive = true) }
            }
        }
    }

    fun playAgain() {
        gameSessionId++
        aiJob?.cancel()

        _uiState.update {
            it.copy(
                board = com.antigravity.tictactoe.domain.model.Board.EMPTY,
                currentPlayer = Player.X,
                gameStatus = GameStatus.Ongoing,
                showResultDialog = false,
                isAiThinking = false,
                isInteractive = true
            )
        }

        checkAndTriggerAiMoveIfNeeded()
    }

    fun newGame() {
        gameSessionId++
        aiJob?.cancel()

        _uiState.update {
            it.copy(
                board = com.antigravity.tictactoe.domain.model.Board.EMPTY,
                currentPlayer = Player.X,
                gameStatus = GameStatus.Ongoing,
                sessionScore = SessionScore(),
                showResultDialog = false,
                isAiThinking = false,
                isInteractive = true
            )
        }

        checkAndTriggerAiMoveIfNeeded()
    }

    fun toggleSound() {
        viewModelScope.launch {
            val current = appSettings.value.soundEnabled
            preferencesRepository.setSoundEnabled(!current)
        }
    }
}
