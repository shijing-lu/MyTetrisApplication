package com.example.mytetrisapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val score: Int,
    val time: Int,
    val date: String,
    val note: String = ""
) 