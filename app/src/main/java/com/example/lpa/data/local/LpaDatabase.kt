package com.example.lpa.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lpa.data.local.dao.EsimProfileDao
import com.example.lpa.data.local.dao.LpaLogEntryDao
import com.example.lpa.data.local.entity.EsimProfileEntity
import com.example.lpa.data.local.entity.LpaLogEntryEntity

/**
 * The single Room database for the PrismEsimLpa application.
 *
 * Registered as an application-scoped singleton via Hilt in
 * [com.example.lpa.app.di.DatabaseModule].
 *
 * ## Schema
 * | Table           | Entity                  | DAO                  |
 * |-----------------|-------------------------|----------------------|
 * | `esim_profiles` | [EsimProfileEntity]     | [EsimProfileDao]     |
 * | `lpa_logs`      | [LpaLogEntryEntity]     | [LpaLogEntryDao]     |
 *
 * ## Versioning
 * Increment [version] and provide a [androidx.room.migration.Migration] in
 * [com.example.lpa.app.di.DatabaseModule] whenever the schema changes.
 * Never use `fallbackToDestructiveMigration()` in production without a
 * data-backup strategy.
 *
 * ## Encryption
 * For production system-app deployment, replace `Room.databaseBuilder` with
 * SQLCipher-backed builder in [com.example.lpa.app.di.DatabaseModule] and
 * source the passphrase from [com.example.lpa.security.keystore.KeystoreManager].
 */
@Database(
    entities = [
        EsimProfileEntity::class,
        LpaLogEntryEntity::class
    ],
    version = 1,
    exportSchema = true  // Enables schema export for version-control and migration tests
)
abstract class LpaDatabase : RoomDatabase() {

    /** Provides access to [EsimProfileEntity] CRUD operations. */
    abstract fun esimProfileDao(): EsimProfileDao

    /** Provides access to [LpaLogEntryEntity] CRUD operations. */
    abstract fun lpaLogEntryDao(): LpaLogEntryDao

    companion object {
        /** Database file name on disk. */
        const val DATABASE_NAME = "lpa_database.db"
    }
}
