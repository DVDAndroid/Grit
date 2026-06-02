package com.shub39.grit.sync.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.shub39.grit.core.habits.domain.SyncQueueOperations

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val operation: SyncQueueOperations,
    val payload: String,
)