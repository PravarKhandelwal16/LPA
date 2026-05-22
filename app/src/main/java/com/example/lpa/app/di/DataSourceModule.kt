package com.example.lpa.app.di

import com.example.lpa.data.datasource.EsimProfileLocalDataSource
import com.example.lpa.data.datasource.LocalEsimProfileDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds **data source interfaces** to their **implementations**.
 *
 * This allows swapping out local data sources (e.g. Room vs DataStore) or
 * remote data sources (e.g. Retrofit vs gRPC) without changing the repository
 * implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindEsimProfileLocalDataSource(
        impl: LocalEsimProfileDataSource
    ): EsimProfileLocalDataSource
}
