package com.example.mytetrisapplication

import android.content.Context
import com.example.mytetrisapplication.Entity.GameRecord
import com.example.mytetrisapplication.database.AppDatabase
import com.example.mytetrisapplication.database.toEntity
import com.example.mytetrisapplication.database.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRecordRepository(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val gameRecordDao = database.gameRecordDao()
    
    // 获取所有记录
    fun getAllRecords(): Flow<List<GameRecord>> {
        return gameRecordDao.getAllRecords().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    // 按得分排序获取记录
    fun getAllRecordsSortedByScore(): Flow<List<GameRecord>> {
        return gameRecordDao.getAllRecordsSortedByScore().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    // 按日期排序获取记录
    fun getAllRecordsSortedByDate(): Flow<List<GameRecord>> {
        return gameRecordDao.getAllRecordsSortedByDate().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    // 保存记录
    suspend fun saveRecord(record: GameRecord) {
        gameRecordDao.insertRecord(record.toEntity())
    }
    
    // 删除记录
    suspend fun deleteRecord(record: GameRecord) {
        gameRecordDao.deleteRecord(record.toEntity())
    }
    
    // 更新记录
    suspend fun updateRecord(record: GameRecord) {
        gameRecordDao.updateRecord(record.toEntity())
    }
}

// 保持向后兼容的函数
suspend fun saveRecord(context: Context, record: GameRecord) {
    val repository = GameRecordRepository(context)
    repository.saveRecord(record)
}

// 为了兼容现有代码，保留loadRecords函数但返回空列表
// 实际的数据加载应该通过Repository的Flow进行
fun loadRecords(context: Context): List<GameRecord> {
    // 这个函数现在已经不推荐使用，因为Room使用Flow异步操作
    // 返回空列表，实际数据应该通过GameRecordRepository.getAllRecords()获取
    return emptyList()
} 