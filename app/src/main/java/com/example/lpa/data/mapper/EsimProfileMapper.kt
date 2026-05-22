package com.example.lpa.data.mapper

import com.example.lpa.data.local.entity.EsimProfileEntity
import com.example.lpa.domain.models.EsimProfile
import com.example.lpa.domain.models.ProfileState

// ─── Entity → Domain ──────────────────────────────────────────────────────────

/**
 * Maps a [EsimProfileEntity] (Room persistence model) to a
 * [EsimProfile] (pure domain model).
 *
 * The domain model uses [ProfileState] derived from the `isActive` flag:
 * - `isActive = true`  → [ProfileState.ENABLED]
 * - `isActive = false` → [ProfileState.DISABLED]
 *
 * Note: [ProfileState.DELETED] is not stored in the database — a deleted
 * profile has its row removed, not flagged.
 */
fun EsimProfileEntity.toDomain(): EsimProfile = EsimProfile(
    iccId           = iccId,
    nickname        = profileName,
    serviceProvider = carrierName,
    state           = if (isActive) ProfileState.ENABLED else ProfileState.DISABLED
)

/**
 * Maps a list of [EsimProfileEntity] to a list of [EsimProfile].
 * Convenience extension on `List` to avoid a manual `map { }` at every call site.
 */
fun List<EsimProfileEntity>.toDomainList(): List<EsimProfile> = map { it.toDomain() }

// ─── Domain → Entity ──────────────────────────────────────────────────────────

/**
 * Maps a [EsimProfile] domain model to a [EsimProfileEntity] for persistence.
 *
 * @param existingId  Pass the existing DB row ID when updating a record;
 *                    leave as `0` for new inserts (Room auto-generates the ID).
 * @param createdAt   Pass the original creation timestamp when updating;
 *                    leave as `System.currentTimeMillis()` for new inserts.
 */
fun EsimProfile.toEntity(
    existingId: Long = 0,
    createdAt: Long = System.currentTimeMillis()
): EsimProfileEntity = EsimProfileEntity(
    id          = existingId,
    profileName = nickname,
    carrierName = serviceProvider,
    isActive    = state == ProfileState.ENABLED,
    iccId       = iccId,
    createdAt   = createdAt,
    updatedAt   = System.currentTimeMillis()
)
