package com.example.mytetrisapplication.Entity

// 记录数据类
data class GameRecord(
    val id: Int = 0,
    val score: Int,
    val time: Int,
    val date: String,
    val note: String = ""
)
