package com.example.mytetrisapplication.ui

import android.graphics.Color
import com.example.mytetrisapplication.ui.GameView.BOARD_COLS
import com.example.mytetrisapplication.ui.GameView.BOARD_ROWS

/**
 * 游戏状态数据类，包含所有需要在UI中显示的数据
 */
data class GameState(
    val board: Array<IntArray> = Array(BOARD_ROWS) { IntArray(BOARD_COLS) { 0 } },
    val activeBlock: List<Pair<Int, Int>> = emptyList(),
    val nextBlockShape: List<Pair<Int, Int>> = emptyList(),
    val score: Int = 0,
    val time: Int = 0,
    val isGameOver: Boolean = false,
    val isPaused: Boolean = false,
    val isStarted: Boolean = false // 新增字段，表示游戏是否已开始
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (!board.contentDeepEquals(other.board)) return false
        if (activeBlock != other.activeBlock) return false
        if (nextBlockShape != other.nextBlockShape) return false
        if (score != other.score) return false
        if (time != other.time) return false
        if (isGameOver != other.isGameOver) return false
        if (isPaused != other.isPaused) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + activeBlock.hashCode()
        result = 31 * result + nextBlockShape.hashCode()
        result = 31 * result + score
        result = 31 * result + time
        result = 31 * result + isGameOver.hashCode()
        result = 31 * result + isPaused.hashCode()
        return result
    }
} 