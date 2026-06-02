package com.shub39.grit.sync.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface SyncDao {
    @Query("SELECT * FROM sync_queue ORDER BY id")
    suspend fun getJobs(): List<SyncQueueEntity>

    @Insert
    suspend fun queueJob(job: SyncQueueEntity): Long

    @Query("DELETE FROM sync_queue WHERE id = :id")
    suspend fun clearJob(id: Long)
}