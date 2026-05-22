package com.example.lpa.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lpa.domain.models.LogLevel

/**
 * Room database entity representing a single LPA operation log entry.
 *
 * Maps to the `lpa_logs` table in [com.example.lpa.data.local.LpaDatabase].
 *
 * Use [com.example.lpa.data.mapper.LpaLogEntryMapper] to convert to/from
 * the [com.example.lpa.domain.models.LpaLogEntry] domain model.
 *
 * @param id        Auto-generated primary key.
 * @param timestamp ISO-8601 timestamp string of when the event occurred.
 * @param operation Human-readable description of the LPA operation.
 * @param level     Severity level stored as a string (Room TypeConverter not required).
 * @param detail    Optional additional context or stack trace snippet.
 * @param createdAt Unix epoch millis for time-based purge queries.
 */
@Entity(tableName = "lpa_logs")
data class LpaLogEntryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "timestamp")
    val timestamp: String,

    @ColumnInfo(name = "operation")
    val operation: String,

    @ColumnInfo(name = "level")
    val level: String = LogLevel.INFO.name,

    @ColumnInfo(name = "detail")
    val detail: String? = null,

    @ColumnInfo(name = "created_at", index = true)
    val createdAt: Long = System.currentTimeMillis()
)
