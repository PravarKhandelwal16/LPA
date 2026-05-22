package com.example.lpa.app.di

import com.example.lpa.data.repository.DeviceStatusRepositoryImpl
import com.example.lpa.data.repository.LogRepositoryImpl
import com.example.lpa.data.repository.ProfileRepositoryImpl
import com.example.lpa.data.repository.SettingsRepositoryImpl
import com.example.lpa.domain.repository.DeviceStatusRepository
import com.example.lpa.domain.repository.LogRepository
import com.example.lpa.domain.repository.ProfileRepository
import com.example.lpa.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds domain repository **interfaces** to their
 * **data layer implementations**.
 *
 * Using [@Binds][Binds] (instead of [@Provides][dagger.Provides]) is more
 * efficient — Dagger generates less code and avoids creating an intermediate
 * factory object.
 *
 * Installed in [SingletonComponent] so all repositories are application-scoped
 * singletons — consistent state across the entire process lifetime.
 *
 * When swapping a stub for a real implementation:
 * 1. Update the return type of the corresponding [@Binds][Binds] function.
 * 2. No changes needed in ViewModels or Use Cases — they only see the interface.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDeviceStatusRepository(
        impl: DeviceStatusRepositoryImpl
    ): DeviceStatusRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindLogRepository(
        impl: LogRepositoryImpl
    ): LogRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
