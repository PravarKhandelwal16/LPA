package com.example.lpa.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing a single eSIM profile record.
 *
 * This is the **persistence model** for the data layer. It maps to the
 * `esim_profiles` table in [com.example.lpa.data.local.LpaDatabase].
 *
 * The domain layer never sees this class — use the mapper extensions in
 * [com.example.lpa.data.mapper.EsimProfileMapper] to convert to/from
 * the [com.example.lpa.domain.models.EsimProfile] domain model.
 *
 * ## Fields
 * @param id           Auto-generated primary key (Room assigns on insert).
 * @param profileName  Human-readable display name for this profile (e.g. "Work SIM").
 * @param carrierName  The Mobile Network Operator or MVNO name (e.g. "T-Mobile").
 * @param isActive     Whether this profile is currently the active/enabled profile.
 * @param iccId        The ICCID from the eUICC — unique identifier per profile.
 *                     Stored as a secondary unique lookup key.
 * @param createdAt    Unix epoch millis when this record was first inserted.
 * @param updatedAt    Unix epoch millis when this record was last updated.
 */
@Entity(tableName = "esim_profiles")
data class EsimProfileEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "profile_name")
    val profileName: String,

    @ColumnInfo(name = "carrier_name")
    val carrierName: String,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = false,

    @ColumnInfo(name = "icc_id", index = true)
    val iccId: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
