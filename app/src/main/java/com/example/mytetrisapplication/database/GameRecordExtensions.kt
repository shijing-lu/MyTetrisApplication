package com.example.mytetrisapplication.database

import com.example.mytetrisapplication.Entity.GameRecord

// 将Entity转换为Domain模型
fun GameRecordEntity.toDomainModel(): GameRecord {
    return GameRecord(
        id = this.id,
        score = this.score,
        time = this.time,
        date = this.date,
        note = this.note
    )
}

// 将Domain模型转换为Entity
fun GameRecord.toEntity(): GameRecordEntity {
    return GameRecordEntity(
        id = this.id,
        score = this.score,
        time = this.time,
        date = this.date,
        note = this.note
    )
} 