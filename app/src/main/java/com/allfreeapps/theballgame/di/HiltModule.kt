package com.allfreeapps.theballgame.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.allfreeapps.theballgame.service.AppDatabase
import com.allfreeapps.theballgame.service.dao.ScoreDao
import com.allfreeapps.theballgame.util.AppLoggerImpl
import com.allfreeapps.theballgame.util.Applogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = androidx.datastore.core.handlers.ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
//            migrations = listOf(androidx.datastore.migrations.SharedPreferencesMigration(context, YOUR_SHARED_PREFS_NAME_IF_MIGRATING)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("app_settings") }
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    @Provides
    fun provideScoreDao(appDatabase: AppDatabase): ScoreDao {
        return appDatabase.scoreDao()
    }

    @Provides
    @Singleton
    fun provideReleaseLogger(@ApplicationContext context: Context): Applogger {
        val logger = AppLoggerImpl()
        logger.initialize(context)
        return logger
    }

}