package com.shub39.grit.sync.repository

import com.shub39.grit.core.data.HabitCompletionAdapter
import com.shub39.grit.core.data.InstantTypeAdapter
import com.shub39.grit.core.data.LocalDateTypeAdapter
import com.shub39.grit.core.habits.domain.HabitCompletion
import com.shub39.grit.core.habits.domain.HabitStatusMinimalKeys
import com.shub39.grit.core.habits.domain.SyncQueueJob
import com.shub39.grit.core.habits.domain.SyncQueueOperations
import com.shub39.grit.core.habits.domain.SyncQueueRepo
import com.shub39.grit.domain.SettingsDatastore
import com.shub39.grit.habits.data.database.HabitEntity
import com.shub39.grit.habits.data.database.HabitStatusDao
import com.shub39.grit.habits.data.database.HabitStatusEntity
import com.shub39.grit.habits.data.database.HabitsDao
import com.shub39.grit.sync.database.SyncDao
import com.shub39.grit.sync.database.SyncQueueEntity
import com.shub39.grit.sync.toSyncQueueJob
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import kotlin.time.Instant

@Single(binds = [SyncQueueRepo::class])
class SyncQueueRepository(
    private val syncDao: SyncDao,
    private val datastore: SettingsDatastore,
    private val habitDao: HabitsDao,
    private val habitStatusDao: HabitStatusDao,
) : SyncQueueRepo {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson {
                registerTypeAdapter(Instant::class.java, InstantTypeAdapter)
                registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter)
                registerTypeAdapter(HabitCompletion::class.java, HabitCompletionAdapter)
            }
        }
        expectSuccess = false
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun getJobs(): List<SyncQueueJob> {
        return syncDao.getJobs().map { it.toSyncQueueJob() }
    }

    override suspend fun queueJob(operation: SyncQueueOperations, payload: String) {
        val job = SyncQueueEntity(operation = operation, payload = payload)
        val id = syncDao.queueJob(job)
        sendJob(job.toSyncQueueJob().copy(id = id))
    }

    override suspend fun clearJob(id: Long) {
        syncDao.clearJob(id)
    }

    override suspend fun sendJob(job: SyncQueueJob) {
        val url = datastore.getSyncServerUrl().firstOrNull() ?: return

        try {
            var ok: Boolean
            when (job.operation) {
                SyncQueueOperations.UPDATE_HABIT -> {
                    val res = client.post("$url/api/habits") {
                        contentType(ContentType.Application.Json)
                        setBody(job.payload)
                    }
                    ok = res.status.isSuccess()
                }

                SyncQueueOperations.DELETE_HABIT -> {
                    return
                    //                val res = client.delete("$url/api/habits") {
                    //                    contentType(ContentType.Application.Json)
                    //                    setBody(job.payload)
                    //                }
                }

                SyncQueueOperations.UPDATE_HABIT_STATUS -> {
                    val objPayload = json.decodeFromString<HabitStatusEntity>(job.payload)
                    val res = client.post("$url/api/habits/${objPayload.habitId}") {
                        contentType(ContentType.Application.Json)
                        setBody(job.payload)
                    }
                    ok = res.status.isSuccess()
                }

                SyncQueueOperations.DELETE_HABIT_STATUS -> {
                    val objPayload = json.decodeFromString<HabitStatusMinimalKeys>(job.payload)
                    val res =
                        client.delete("$url/api/habits/${objPayload.habitId}/${objPayload.date}")
                    ok = res.status.isSuccess()
                }
            }
            if (ok) {
                clearJob(job.id)
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun elaborateQueue() {
        val jobs = getJobs()
        for (j in jobs) sendJob(j)
    }

    override suspend fun importAll() {
        val url = datastore.getSyncServerUrl().firstOrNull() ?: return

        habitDao.deleteAllHabits()
        habitStatusDao.deleteAllHabitStatus()

        val habits = client.get("$url/api/habits").body<List<HabitEntity>>()

        for (h in habits) {
            val habitStatuses = client.get("$url/api/habits/${h.id}").body<List<HabitStatusEntity>>()

            habitDao.upsertHabit(h)
            for (hs in habitStatuses) {
                habitStatusDao.upsertHabitStatus(hs)
            }
        }

        val jobs = getJobs()
        for (j in jobs) clearJob(j.id)
    }
}