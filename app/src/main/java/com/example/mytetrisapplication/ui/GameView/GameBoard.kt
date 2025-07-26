package com.example.mytetrisapplication.ui.GameView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawBlockCell(
    row: Int,
    col: Int,
    cellWidth: Float,
    cellHeight: Float,
    cellSpacing: Float = 4f,
    fillColor: Color = Color.Black,
    borderWidth: Float = 5f
) {
    val left = col * cellWidth + cellSpacing / 2
    val top = row * cellHeight + cellSpacing / 2
    val width = cellWidth - cellSpacing
    val height = cellHeight - cellSpacing
    val topLeft = Offset(left, top)
    val size = Size(width, height)
    // 填充
    drawRect(
        color = fillColor,
        topLeft = topLeft,
        size = size,
        style = Fill
    )
    // 边框
    drawRect(
        color = fillColor,
        topLeft = topLeft,
        size = size,
        style = Stroke(width = borderWidth)
    )
    // 大边框
    drawRect(
        color = Color.White,
        topLeft = Offset(topLeft.x + BlockCellStrokeWidth, topLeft.y + BlockCellStrokeWidth),
        size = Size(size.width - 2 * BlockCellStrokeWidth, size.height - 2 * BlockCellStrokeWidth),
        style = Stroke(width = BlockCellStrokeWidth)
    )
}
@Composable
fun GameBoard(
    board: Array<IntArray>,
    activeBlock: List<Pair<Int, Int>>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(CardCornerRadius))
            .padding(CardPadding),
        shape = RoundedCornerShape(CardCornerRadius),
        elevation = CardDefaults.cardElevation(CardElevation)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellWidth = size.width / BOARD_COLS
            val cellHeight = size.height / BOARD_ROWS
            // 绘制棋盘背景
            for (row in 0 until BOARD_ROWS) {
                for (col in 0 until BOARD_COLS) {
                    if (board[row][col] != 0){
                        drawBlockCell(
                            row = row,
                            col = col,
                            cellWidth = cellWidth,
                            cellHeight = cellHeight,
                            cellSpacing = BlockCellSpacing // 可调整
                        )
                    }
                }
            }
            // 绘制活动方块
            for ((row, col) in activeBlock) {
                if (row in 0 until BOARD_ROWS && col in 0 until BOARD_COLS) {
                    drawBlockCell(
                        row = row,
                        col = col,
                        cellWidth = cellWidth,
                        cellHeight = cellHeight,
                        cellSpacing = BlockCellSpacing // 可调整
                    )
                }
            }
            // 绘制网格线
            val gridColor = Color.LightGray
            for (i in 1 until BOARD_COLS) {
                drawLine(
                    color = gridColor,
                    start = Offset(i * cellWidth, 0f),
                    end = Offset(i * cellWidth, size.height),
                    strokeWidth = 1f
                )
            }
            for (i in 1 until BOARD_ROWS) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, i * cellHeight),
                    end = Offset(size.width, i * cellHeight),
                    strokeWidth = 1f
                )
            }
        }
    }
}


