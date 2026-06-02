package com.shub39.grit.core.habits.domain

interface SyncQueueRepo {
    suspend fun getJobs(): List<SyncQueueJob>
    suspend fun queueJob(operation: SyncQueueOperations, payload: String)
    suspend fun clearJob(id: Long)

    suspend fun sendJob(job: SyncQueueJob)
    suspend fun elaborateQueue()
    suspend fun importAll()
}