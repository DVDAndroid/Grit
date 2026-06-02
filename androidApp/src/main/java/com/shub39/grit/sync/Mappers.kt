package com.shub39.grit.sync

import com.shub39.grit.core.habits.domain.SyncQueueJob
import com.shub39.grit.sync.database.SyncQueueEntity


fun SyncQueueEntity.toSyncQueueJob() : SyncQueueJob {
    return SyncQueueJob(id = id, operation = operation, payload = payload)
}

fun SyncQueueJob.toSyncQueueJobEntity() : SyncQueueEntity {
    return SyncQueueEntity(id = id, operation = operation, payload = payload)
}