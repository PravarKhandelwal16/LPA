package com.example.lpa.app.di

import android.content.Context
import androidx.room.Room
import com.example.lpa.data.local.LpaDatabase
import com.example.lpa.data.local.dao.EsimProfileDao
import com.example.lpa.data.local.dao.LpaLogEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides the Room [LpaDatabase] and its DAOs as
 * application-scoped singletons.
 *
 * ## Why @Provides instead of @Binds?
 * [LpaDatabase] is constructed by `Room.databaseBuilder()` — there is no
 * `@Inject constructor` to bind, so `@Provides` is the correct approach.
 *
 * ## Database location
 * The database file is stored at the standard Android app-private path:
 * `/data/data/com.example.lpa/databases/lpa_database.db`
 *
 * ## Encryption
 * For production system-app hardening, replace `Room.databaseBuilder` with a
 * SQLCipher-backed builder here. The DAO injection points require no changes.
 *
 * ## Migration strategy
 * Add [androidx.room.migration.Migration] objects to the builder's
 * `.addMigrations(MIGRATION_1_2, ...)` call when [LpaDatabase.version] increments.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the singleton [LpaDatabase] instance.
     *
     * [SingletonComponent] ensures only one database connection is created
     * for the entire application process lifetime — critical for correct
     * Room Flow invalidation and WAL mode behaviour.
     *
     * @param context The application [Context] provided by Hilt.
     */
    @Provides
    @Singleton
    fun provideLpaDatabase(
        @ApplicationContext context: Context
    ): LpaDatabase = Room.databaseBuilder(
        context,
        LpaDatabase::class.java,
        LpaDatabase.DATABASE_NAME
    )
        // Uncomment when adding schema migrations:
        // .addMigrations(MIGRATION_1_2)
        // For development only — remove before production release:
        .fallbackToDestructiveMigration()
        .build()

    /**
     * Provides [EsimProfileDao] sourced from the singleton [LpaDatabase].
     *
     * Injected into [com.example.lpa.data.repository.ProfileRepositoryImpl].
     */
    @Provides
    @Singleton
    fun provideEsimProfileDao(database: LpaDatabase): EsimProfileDao =
        database.esimProfileDao()

    /**
     * Provides [LpaLogEntryDao] sourced from the singleton [LpaDatabase].
     *
     * Injected into [com.example.lpa.data.repository.LogRepositoryImpl].
     */
    @Provides
    @Singleton
    fun provideLpaLogEntryDao(database: LpaDatabase): LpaLogEntryDao =
        database.lpaLogEntryDao()
}
