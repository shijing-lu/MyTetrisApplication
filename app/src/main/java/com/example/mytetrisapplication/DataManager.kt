package com.example.mytetrisapplication

import android.content.Context
import com.example.mytetrisapplication.database.AppDatabase
import com.example.mytetrisapplication.database.GameRecordEntity
import kotlinx.coroutines.flow.Flow

class GameRecordRepository(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val gameRecordDao = database.gameRecordDao()
    
    // 获取所有记录
    fun getAllRecords(): Flow<List<GameRecordEntity>> {
        return gameRecordDao.getAllRecords()
    }
    
    // 按得分排序获取记录
    fun getAllRecordsSortedByScore(): Flow<List<GameRecordEntity>> {
        return gameRecordDao.getAllRecordsSortedByScore()
    }
    
    // 按日期排序获取记录
    fun getAllRecordsSortedByDate(): Flow<List<GameRecordEntity>> {
        return gameRecordDao.getAllRecordsSortedByDate()
    }
    
    // 保存记录
    suspend fun saveRecord(record: GameRecordEntity) {
        gameRecordDao.insertRecord(record)
    }
    
    // 删除记录
    suspend fun deleteRecord(record: GameRecordEntity) {
        gameRecordDao.deleteRecord(record)
    }
    
    // 更新记录
    suspend fun updateRecord(record: GameRecordEntity) {
        gameRecordDao.updateRecord(record)
    }
} 