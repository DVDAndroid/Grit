package com.shub39.grit.sync.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import com.shub39.grit.core.data.Converters

@Database(
    entities = [SyncQueueEntity::class],
    version = SyncDatabase.SCHEMA_VERSION,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class SyncDatabase : RoomDatabase() {

    abstract fun syncDao(): SyncDao

    companion object {
        const val SCHEMA_VERSION = 1
        const val DB_NAME = "sync"
    }
}
