package com.example.mytetrisapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.mytetrisapplication.ui.GameView.Block

class GameViewModel : ViewModel() {
    val board: Array<IntArray> = Array(com.example.mytetrisapplication.ui.GameView.BOARD_ROWS) { IntArray(com.example.mytetrisapplication.ui.GameView.BOARD_COLS) { 0 } }
    var currentBlock: Block = Block.random(com.example.mytetrisapplication.ui.GameView.BOARD_ROWS, com.example.mytetrisapplication.ui.GameView.BOARD_COLS)
    var score: Int = 0
    var isGameOver: Boolean = false
    var nextBlock: Block = Block.random(com.example.mytetrisapplication.ui.GameView.BOARD_ROWS, com.example.mytetrisapplication.ui.GameView.BOARD_COLS)

    // 游戏状态Flow
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // 游戏计时器
    private var gameTimer: kotlinx.coroutines.Job? = null
    private var autoDropTimer: kotlinx.coroutines.Job? = null


    private fun canPlace(block: Block): Boolean {
        for ((r, c) in block.absoluteCoords()) {
            if (r !in 0 until board.size || c !in 0 until board[0].size) return false
            if (board[r][c] != 0) return false
        }
        return true
    }
    private fun fixBlock() {
        for ((r, c) in currentBlock.absoluteCoords()) {
            if (r in 0 until board.size && c in 0 until board[0].size) {
                board[r][c] = currentBlock.type
            }
        }
    }
    private fun clearLines(): Int {
        var lines = 0
        val newBoard = board.filter { row -> row.any { it == 0 } }.toMutableList()
        lines = board.size - newBoard.size
        repeat(lines) { newBoard.add(0, IntArray(board[0].size) { 0 }) }
        for (i in board.indices) board[i] = newBoard[i]
        score += lines * 100
        return lines
    }
    private fun spawnBlock() {
        currentBlock = nextBlock
        nextBlock = Block.random(board.size, board[0].size)
        if (!canPlace(currentBlock)) isGameOver = true
    }
    private fun transformBlock(transform: (Block) -> Block) {
        val moved = transform(currentBlock)
        if (canPlace(moved)) currentBlock = moved
    }
    private fun getRenderBoard(): Pair<Array<IntArray>, List<Pair<Int, Int>>> {
        val tempBoard = Array(board.size) { board[it].clone() }
        val active = currentBlock.absoluteCoords()
        return Pair(tempBoard, active)
    }
    private fun getNextBlockShape(): List<Pair<Int, Int>> = nextBlock.shape
    

    init {
        resetGameState()
    }
    
    /**
     * 处理游戏事件
     */
    
    /**
     * 开始新游戏
     */
    fun startNewGame() {
        // 重置游戏引擎
        board.forEach { it.fill(0) }
        score = 0
        isGameOver = false
        spawnBlock()
        // 更新状态
        updateGameState(isStarted = true)
        
        // 启动计时器
        startTimers()
    }

    /**
     * 初始化但不开始游戏
     */
    fun resetGameState() {
        board.forEach { it.fill(0) }
        score = 0
        isGameOver = false
        // 不生成方块
        _gameState.value = GameState(isStarted = false)
        stopTimers()
    }
    
    /**
     * 启动游戏计时器
     */
    private fun startTimers() {
        // 停止现有计时器
//        stopTimers()
        
        // 游戏时间计时器
        gameTimer = viewModelScope.launch {
            while (!_gameState.value.isGameOver && !_gameState.value.isPaused) {
                delay(1000)
                _gameState.update { it.copy(time = it.time + 1) }
            }
        }
        
        // 自动下落计时器
        autoDropTimer = viewModelScope.launch {
            while (!_gameState.value.isGameOver && !_gameState.value.isPaused) {
                delay(500)
                moveDown()
            }
        }
    }
    
    /**
     * 停止计时器
     */
    private fun stopTimers() {
        gameTimer?.cancel()
        autoDropTimer?.cancel()
    }
    
    /**
     * 暂停游戏
     */
    fun pauseGame() {
        _gameState.update { it.copy(isPaused = true) }
        stopTimers()
    }
    
    /**
     * 恢复游戏
     */
    fun resumeGame() {
        _gameState.update { it.copy(isPaused = false) }
        startTimers()
    }
    
    /**
     * 向左移动
     */
    fun moveLeft() {
        viewModelScope.launch(Dispatchers.Default) {
            if (!_gameState.value.isStarted || _gameState.value.isGameOver || _gameState.value.isPaused) return@launch
            // moveLeftInternal 合并
            transformBlock { it.copy(col = it.col - 1) }
            withContext(Dispatchers.Main) { updateGameState() }
        }
    }
    
    /**
     * 向右移动
     */
    fun moveRight() {
        viewModelScope.launch(Dispatchers.Default) {
            if (!_gameState.value.isStarted || _gameState.value.isGameOver || _gameState.value.isPaused) return@launch
            // moveRightInternal 合并
            transformBlock { it.copy(col = it.col + 1) }
            withContext(Dispatchers.Main) { updateGameState() }
        }
    }
    
    /**
     * 向下移动
     */
    fun moveDown() {
        viewModelScope.launch(Dispatchers.Default) {
            if (!_gameState.value.isStarted || _gameState.value.isGameOver || _gameState.value.isPaused) return@launch
            //   合并
            val moved = run {
                val movedBlock = currentBlock.copy(row = currentBlock.row + 1)
                if (canPlace(movedBlock)) {
                    currentBlock = movedBlock
                    true
                } else {
                    fixBlock()
                    clearLines()
                    spawnBlock()
                    false
                }
            }
            withContext(Dispatchers.Main) {
                updateGameState()
                if (_gameState.value.isGameOver) stopTimers()
            }
        }
    }
    
    /**
     * 旋转方块
     */
    fun rotate() {
        viewModelScope.launch(Dispatchers.Default) {
            if (!_gameState.value.isStarted || _gameState.value.isGameOver || _gameState.value.isPaused) return@launch
            // rotateInternal 合并
            val type = currentBlock.type
            val center = currentBlock.shape[1]
            transformBlock { block ->
                val rotated = when (type) {
                    2 -> block
                    else -> {
                        val rotatedShape = block.shape.map { (r, c) ->
                            val dr = r - center.first
                            val dc = c - center.second
                            Pair(center.first - dc, center.second + dr)
                        }
                        block.copy(shape = rotatedShape)
                    }
                }
                if (canPlace(rotated)) return@transformBlock rotated
                val left = rotated.copy(col = rotated.col - 1)
                if (canPlace(left)) return@transformBlock left
                val right = rotated.copy(col = rotated.col + 1)
                if (canPlace(right)) return@transformBlock right
                if (type == 1) {
                    val left2 = rotated.copy(col = rotated.col - 2)
                    if (canPlace(left2)) return@transformBlock left2
                    val right2 = rotated.copy(col = rotated.col + 2)
                    if (canPlace(right2)) return@transformBlock right2
                }
                block
            }
            withContext(Dispatchers.Main) { updateGameState() }
        }
    }
    
    /**
     * 更新游戏状态
     */
    private fun updateGameState(isStarted: Boolean? = null) {
        val (board, active) = getRenderBoard()
        val nextShape = getNextBlockShape()
        _gameState.update { currentState ->
            currentState.copy(
                board = board,
                activeBlock = active,
                nextBlockShape = nextShape,
                score = score,
                isGameOver = isGameOver,
                isStarted = isStarted ?: currentState.isStarted
            )
        }
    }

    fun hardDrop() {
        viewModelScope.launch(Dispatchers.Default) {
            if (!_gameState.value.isStarted || _gameState.value.isGameOver || _gameState.value.isPaused) return@launch
            // hardDropInternal 合并
            while (true) {
                val movedBlock = currentBlock.copy(row = currentBlock.row + 1)
                if (canPlace(movedBlock)) {
                    currentBlock = movedBlock
                } else {
                    fixBlock()
                    clearLines()
                    spawnBlock()
                    break
                }
            }
            withContext(Dispatchers.Main) {
                updateGameState()
                if (_gameState.value.isGameOver) stopTimers()
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        stopTimers()
    }
} 