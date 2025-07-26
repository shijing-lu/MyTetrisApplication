package com.example.mytetrisapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {
    @Query("SELECT * FROM game_records ORDER BY id DESC")
    fun getAllRecords(): Flow<List<GameRecordEntity>>
    
    @Query("SELECT * FROM game_records ORDER BY score DESC")
    fun getAllRecordsSortedByScore(): Flow<List<GameRecordEntity>>
    
    @Query("SELECT * FROM game_records ORDER BY date DESC")
    fun getAllRecordsSortedByDate(): Flow<List<GameRecordEntity>>
    
    @Insert
    suspend fun insertRecord(record: GameRecordEntity)
    
    @Delete
    suspend fun deleteRecord(record: GameRecordEntity)
    
    @Update
    suspend fun updateRecord(record: GameRecordEntity)
    
    @Query("DELETE FROM game_records WHERE id = :recordId")
    suspend fun deleteRecordById(recordId: Int)
} 