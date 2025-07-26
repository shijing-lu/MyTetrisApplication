package com.example.mytetrisapplication.ui.GameView

data class Block(
    var shape: List<Pair<Int, Int>>, // 相对坐标
    var row: Int, // 左上角行
    var col: Int, // 左上角列
    val type: Int // 1-7
) {
    companion object {
        private val shapes = listOf(
            listOf(Pair(0,0), Pair(0,1), Pair(0,2), Pair(0,3)),
            listOf(Pair(0,0), Pair(0,1), Pair(1,0), Pair(1,1)),
            listOf(Pair(0,1), Pair(1,1),Pair(1 ,0),  Pair(1,2)),
            listOf(Pair(0,1), Pair(1,1),Pair(0,2), Pair(1,0)),
            listOf(Pair(0,0), Pair(1,1), Pair(0,1), Pair(1,2)),
            listOf(Pair(0,0), Pair(1,1),Pair(1,0),  Pair(1,2)),
            listOf(Pair(0,2),  Pair(1,1), Pair(1,0),Pair(1,2))
        )
        fun random(numRows: Int, numCols: Int): Block {
            val type = kotlin.random.Random.nextInt(1, 8)
            val shape = shapes[type-1]
            return Block(shape, 0, numCols/2-2, type)
        }
    }
    fun absoluteCoords(): List<Pair<Int, Int>> =
        shape.map { (dr, dc) -> Pair(row + dr, col + dc) }
} 