package com.example.lpa.data.mapper

import com.example.lpa.data.local.entity.LpaLogEntryEntity
import com.example.lpa.domain.models.LpaLogEntry
import com.example.lpa.domain.models.LogLevel

// ─── Entity → Domain ──────────────────────────────────────────────────────────

/**
 * Maps a [LpaLogEntryEntity] (Room persistence model) to a
 * [LpaLogEntry] (pure domain model).
 *
 * [LogLevel] is stored as a String in the DB. If an unknown value is
 * encountered (e.g. from a future schema migration), it falls back to [LogLevel.INFO].
 */
fun LpaLogEntryEntity.toDomain(): LpaLogEntry = LpaLogEntry(
    id        = id.toString(),
    timestamp = timestamp,
    operation = operation,
    level     = runCatching { LogLevel.valueOf(level) }.getOrDefault(LogLevel.INFO),
    detail    = detail
)

/**
 * Maps a list of [LpaLogEntryEntity] to a list of [LpaLogEntry].
 */
fun List<LpaLogEntryEntity>.toDomainList(): List<LpaLogEntry> = map { it.toDomain() }

// ─── Domain → Entity ──────────────────────────────────────────────────────────

/**
 * Maps a [LpaLogEntry] domain model to a [LpaLogEntryEntity] for persistence.
 *
 * @param createdAt Unix epoch millis for the `created_at` column used in
 *                  time-range purge queries. Defaults to now.
 */
fun LpaLogEntry.toEntity(
    createdAt: Long = System.currentTimeMillis()
): LpaLogEntryEntity = LpaLogEntryEntity(
    id        = 0,          // 0 = let Room auto-generate the primary key
    timestamp = timestamp,
    operation = operation,
    level     = level.name,
    detail    = detail,
    createdAt = createdAt
)
