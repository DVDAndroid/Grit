package com.shub39.grit.sync.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import org.koin.core.annotation.Single

@Single
class SyncDbFactory(private val context: Context) {
    fun create(): RoomDatabase.Builder<SyncDatabase> {
        val appContext = context.applicationContext

        return Room.databaseBuilder(appContext, SyncDatabase::class.java, SyncDatabase.DB_NAME)
    }
}
